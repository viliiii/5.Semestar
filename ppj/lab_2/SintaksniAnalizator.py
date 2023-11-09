retci = []
while True:
    try:
        line = input()
    except EOFError:
        break
    retci.append(line)

# print(retci)
# retci = ['IDN 1 a', 'OP_PRIDRUZI 1 =', 'BROJ 1 0']
ind = 0

tree = []
space_num = 0


# Moraš ovaj space_num obrisat, da nije globalna
# i napravit da svaka funkcija prima argument koji će bit broj razmaka
# koji se upotrebljava za njene listove, a svakoj funkciji koju ona zove predaj
# broj razmaka trenutne + 1

# sebe ispisat sa razmaci * i
# sve kaj je u meni i sve kaj pozivam sa *i+1
def program(i):
    global space_num
    tree.append(" " * i + "<program>")
    space_num += 1
    global ind
    if ind >= len(retci):
        lista_naredbi(i + 1)
        return
    if ind >= len(retci) or retci[ind][:3] == "IDN" or retci[ind][:5] == "KR_ZA":
        lista_naredbi(i + 1)
    else:
        tree.append(f"err {retci[ind]}")


def lista_naredbi(i):
    global space_num
    tree.append(" " * i + "<lista_naredbi>")
    space_num += 1
    global ind
    if ind >= len(retci):
        tree.append(" " * (i + 1) + "$")
        space_num -= 2
        return

    if ind >= len(retci) or retci[ind][:5] == "KR_AZ":
        tree.append(" " * (i + 1) + "$")
        space_num -= 2
    elif retci[ind][:3] == "IDN" or retci[ind][:5] == "KR_ZA":
        naredba(i + 1)
        lista_naredbi(i + 1)
    else:
        tree.append(f"err {retci[ind]}")


def naredba(i):
    global space_num
    tree.append(" " * i + "<naredba>")
    space_num += 1
    global ind
    if retci[ind][:3] == "IDN":
        naredba_pridruzivanja(i + 1)
    elif retci[ind][:5] == "KR_ZA":
        za_petlja(i + 1)
    else:
        tree.append(f"err {retci[ind]}")


def naredba_pridruzivanja(i):
    global space_num
    tree.append(" " * i + "<naredba_pridruzivanja>")
    space_num += 1
    global ind
    if retci[ind][:3] == "IDN":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        if retci[ind][:5] == "OP_PR":
            tree.append(" " * (i + 1) + retci[ind])
            ind += 1
            e(i + 1)
        else:
            tree.append(f"err {retci[ind]}")
    else:
        tree.append(f"err {retci[ind]}")


def za_petlja(i):
    global space_num
    tree.append(" " * i + "<za_petlja>")
    space_num += 1
    global ind
    if not (ind >= len(retci)) and retci[ind][:5] == "KR_ZA":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        if not (ind >= len(retci)) and retci[ind][:3] == "IDN":
            tree.append(" " * (i + 1) + retci[ind])
            ind += 1
            if not (ind >= len(retci)) and retci[ind][:5] == "KR_OD":
                tree.append(" " * (i + 1) + retci[ind])
                ind += 1
                e(i + 1)
                if not (ind >= len(retci)) and retci[ind][:5] == "KR_DO":
                    tree.append(" " * (i + 1) + retci[ind])
                    ind += 1
                    e(i + 1)
                    lista_naredbi(i + 1)
                    if not (ind >= len(retci)) and retci[ind][:5] == "KR_AZ":
                        tree.append(" " * (i + 1) + retci[ind])
                        space_num -= 2
                        ind += 1
                    else:
                        tree.append(f"err {retci[ind - 1]}")
                        return

                else:
                    tree.append(f"err {retci[ind - 1]}")

            else:
                tree.append(f"err {retci[ind - 1]}")

        else:
            tree.append(f"err {retci[ind - 1]}")

    else:
        tree.append(f"err {retci[ind - 1]}")


def e(i):
    global space_num
    tree.append(" " * i + "<E>")
    space_num += 1
    global ind

    if ind >= len(retci):
        tree.append("err kraj")
        return
    if (retci[ind][:3] == "IDN" or retci[ind][:4] == "BROJ" or
            retci[ind][:7] == "OP_PLUS" or retci[ind][:8] == "OP_MINUS" or
            retci[ind][:9] == "L_ZAGRADA"):
        t(i + 1)
        e_lista(i + 1)
    else:
        tree.append(f"err {retci[ind]}")


def e_lista(i):
    global space_num
    tree.append(" " * i + "<E_lista>")
    space_num += 1
    global ind
    if ind >= len(retci):
        tree.append(" " * (i + 1) + "$")
        space_num -= 4
        return

    if (ind >= len(retci) or retci[ind][:3] == "IDN" or retci[ind][:5] == "KR_ZA"
            or retci[ind][:5] == "KR_DO" or retci[ind][:5] == "KR_AZ" or retci[ind][:9] == "D_ZAGRADA"):
        tree.append(" " * (i + 1) + "$")

        if retci[ind][:3] == "IDN" or retci[ind][:5] == "KR_ZA":
            space_num -= 4
        elif retci[ind][:5] == "KR_AZ":
            space_num -= 8
        else:
            space_num -= 2

    elif retci[ind][:8] == "OP_MINUS":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        e(i + 1)
    elif retci[ind][:7] == "OP_PLUS":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        e(i + 1)
    else:
        tree.append(f"err {retci[ind]}")


def t(i):
    global space_num
    tree.append(" " * i + "<T>")
    space_num += 1
    global ind
    if (retci[ind][:3] == "IDN" or retci[ind][:4] == "BROJ" or
            retci[ind][:7] == "OP_PLUS" or retci[ind][:8] == "OP_MINUS" or
            retci[ind][:9] == "L_ZAGRADA"):
        p(i + 1)
        t_lista(i + 1)
    else:
        tree.append(f"err {retci[ind]}")


def t_lista(i):
    global space_num
    tree.append(" " * i + "<T_lista>")
    space_num += 1
    global ind

    if ind >= len(retci):
        tree.append(" " * (i + 1) + "$")
        space_num -= 4
        return

    if (ind >= len(retci) or retci[ind][:3] == "IDN" or retci[ind][:5] == "KR_ZA"
            or retci[ind][:5] == "KR_DO" or retci[ind][:5] == "KR_AZ" or retci[ind][:9] == "D_ZAGRADA"
            or retci[ind][:7] == "OP_PLUS" or retci[ind][:8] == "OP_MINUS"):
        tree.append(" " * (i + 1) + "$")

        if ind + 1 < len(retci):
            if (retci[ind][:7] == "OP_PLUS" and retci[ind + 1][:3] == "IDN") or retci[ind][:5] == "KR_AZ":
                space_num -= 4
            else:
                space_num -= 2
        else:
            if retci[ind][:5] == "KR_AZ":
                space_num -= 4

        # if retci[ind][:5] == "KR_AZ":
        #     space_num -= 4
        # elif retci[ind][:7] == "OP_PLUS":
        #     if ind + 1 < len(retci):
        #         if retci[ind + 1][:3] == "IDN":
        #             space_num -= 4
        # else:
        #     space_num -= 2

        # if (retci[ind][:7] == "OP_PLUS" and retci[ind + 1][:3] == "IDN") or retci[ind][:5] == "KR_AZ":
        #     space_num -= 4
        # else:
        #     space_num -= 2


    elif retci[ind][:9] == "OP_DIJELI":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        t(i + 1)
    elif retci[ind][:7] == "OP_PUTA":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        t(i + 1)
    else:
        tree.append(f"err {retci[ind]}")


def p(i):
    global space_num
    tree.append(" " * i + "<P>")
    space_num += 1
    global ind
    if retci[ind][:3] == "IDN":
        tree.append(" " * (i + 1) + retci[ind])
        space_num -= 1
        ind += 1
    elif retci[ind][:4] == "BROJ":
        tree.append(" " * (i + 1) + retci[ind])
        space_num -= 1
        ind += 1
    elif retci[ind][:4] == "OP_P":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        p(i + 1)
    elif retci[ind][:4] == "OP_M":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        p(i + 1)
    elif retci[ind][:5] == "L_ZAG":
        tree.append(" " * (i + 1) + retci[ind])
        ind += 1
        e(i + 1)
        if retci[ind][:5] == "D_ZAG":
            tree.append(" " * (i + 1) + retci[ind])
            space_num -= 1
            ind += 1
        else:
            tree.append(f"err {retci[ind]}")
    else:
        tree.append(f"err {retci[ind]}")


program(0)

flag = False

for l in tree:
    if "err" in l:
        print(l)
        flag = True
        break

if not flag:
    for j in range(0, len(tree)):
        print(tree[j])
