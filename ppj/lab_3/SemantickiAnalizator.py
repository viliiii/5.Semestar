retci = []
while True:
    try:
        line = input()
    except EOFError:
        break
    retci.append(line.strip())

# globalni indeks za hod kroz retke inputa
ind = 1

output = []

# rjecnik:   ime, redak
definirane = {}


#Biti će lista rjecnika, varijable definirane u forovima i ugnjezdenim forovima
forovi = []
ind_forovi = -1


def program():
    global ind
    if retci[ind] == "<lista_naredbi>":
        ind += 1
        lista_naredbi(False)


def lista_naredbi(za):
    global ind

    if ind >= len(retci):
        return

    if retci[ind] == "<naredba>":
        ind += 1
        naredba(za)
    if retci[ind] == "<lista_naredbi>":
        ind += 1
        lista_naredbi(za)
    else:
        ind += 1


def naredba(za):
    global ind

    if ind >= len(retci):
        return

    if retci[ind] == "<naredba_pridruzivanja>":
        ind += 1
        naredba_pridruzivanja(za)

    if retci[ind] == "<za_petlja>":
        ind += 1
        za_petlja()


# parametar za nam govori je li metoda pozvana iz za petlje ili ne
def naredba_pridruzivanja(za):
    global ind

    if ind >= len(retci):
        return

    ime_var = retci[ind].split(" ")[2]
    redak_def_var = retci[ind].split(" ")[1]

    ind += 2

    if retci[ind] == "<E>":
        ind += 1
        e(za)

    if za:
        definirana_u_foru_flag = False
        if len(forovi) > 0:
            for i in range(len(forovi) - 1, -1, -1):
                if ime_var in forovi[i]:
                    definirana_u_foru_flag = True
                    ind_fora = i
                    break
        if not definirana_u_foru_flag:
            if ime_var not in definirane:
                forovi[len(forovi)-1][ime_var] = redak_def_var
    else:
        if ime_var not in definirane:
            definirane[ime_var] = redak_def_var


def za_petlja():
    global ind, ind_forovi
    ind += 1
    ime_var = retci[ind].split(" ")[2]
    redak_def_var = retci[ind].split(" ")[1]

    skrivanje = False
    if ime_var in definirane:
        skrivanje = True
        vanjska_redak = definirane[ime_var]
        definirane.pop(ime_var)

    ind += 2
    ind_forovi += 1
    # od
    if retci[ind] == "<E>":
        ind += 1
        e(True)

    if retci[ind][:5] == "KR_DO":
        ind += 1
    # do
    if retci[ind] == "<E>":
        ind += 1
        e(True)


    forovi.append({ime_var: redak_def_var})

    if retci[ind] == "<lista_naredbi>":
        lista_naredbi(True)

    if retci[ind][:5] == "KR_AZ":
        forovi.pop()
        if skrivanje:
            definirane[ime_var] = vanjska_redak
        ind += 1


def e(za):
    global ind
    if retci[ind][:3] == "<T>":
        ind += 1
        t(za)

    if retci[ind] == "<E_lista>":
        ind += 1
        e_lista(za)


def t(za):
    global ind

    if retci[ind] == "<P>":
        ind += 1
        p(za)

    if retci[ind] == "<T_lista>":
        ind += 1
        t_lista(za)


def p(za):
    global ind, ind_forovi

    if ind >= len(retci):
        return

    if retci[ind][:4] == "BROJ":
        ind += 1
        return
    elif retci[ind][:4] == "OP_P":
        ind += 2
        p(za)
        return
    elif retci[ind][:4] == "OP_M":
        ind += 2
        p(za)
        return
    elif retci[ind][:5] == "L_ZAG":
        ind += 2
        e(za)
        if retci[ind][:5] == "D_ZAG":
            ind += 1
            return

    ime_var = retci[ind].split(" ")[2]
    redak_koristenja_var = retci[ind].split(" ")[1]

    if za:
        definirana_u_foru_flag = False
        ind_fora = -1

        if len(forovi) > 0:
            for i in range(len(forovi) - 1, -1, -1):
                if ime_var in forovi[i]:
                    definirana_u_foru_flag = True
                    ind_fora = i
                    break

        if definirana_u_foru_flag:
            output.append(f"{redak_koristenja_var} {forovi[ind_fora][ime_var]} {ime_var}")
        elif ime_var in definirane:
            output.append(f"{redak_koristenja_var} {definirane[ime_var]} {ime_var}")
        else:
            output.append(f"err {redak_koristenja_var} {ime_var}")
            return
    else:
        if ime_var in definirane:
            output.append(f"{redak_koristenja_var} {definirane[ime_var]} {ime_var}")
        else:
            output.append(f"err {redak_koristenja_var} {ime_var}")
            return

    ind += 1


def t_lista(za):
    global ind
    ind += 1
    if retci[ind][:3] == "<T>":
        ind += 1
        t(za)


def e_lista(za):
    global ind
    ind += 1
    if retci[ind] == "<E>":
        ind += 1
        e(za)


program()

for line in output:
    print(line)
    if "err" in line:
        break
