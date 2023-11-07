retci = []
while True:
    try:
        line = input()
    except EOFError:
        break
    retci.append(line)

lastNumFlag = False

for i in range(0, len(retci)):
    idn = ''
    broj = ''
    for j in range(0, len(retci[i])):


        if (j+1) < len(retci[i]):
            if retci[i][j] == '/' and retci[i][j + 1] == '/':
                break

        if 'A' <= retci[i][j] <= 'Z' or 'a' <= retci[i][j] <= 'z':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            idn = idn + retci[i][j]

        if (retci[i][j] == ' ' or j == (len(retci[i]) - 1)) and idn != '':
            if idn == 'za':
                print(f"KR_ZA {(i + 1)} {idn}")
                idn = ''
            elif idn == 'od':
                print(f"KR_OD {(i + 1)} {idn}")
                idn = ''
            elif idn == 'do':
                print(f"KR_DO {(i + 1)} {idn}")
                idn = ''
            elif idn == 'az':
                print(f"KR_AZ {(i + 1)} {idn}")
                idn = ''
            else:
                if retci[i][j].isdigit():
                    idn = idn + retci[i][j]
                    lastNumFlag = True


                print(f"IDN {(i + 1)} {idn}")
                idn = ''
                if lastNumFlag:
                    lastNumFlag = False
                    break




        if retci[i][j].isdigit() and idn == '':
            broj = broj + retci[i][j]

        if retci[i][j].isdigit() and idn != '':
            idn = idn + retci[i][j]

        if (retci[i][j] == ' ' or j == (len(retci[i]) - 1)) and broj != '':
            print(f"BROJ {(i + 1)} {broj}")
            broj = ''





        if retci[i][j] == '=':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            if idn != '':
                print(f"IDN {(i + 1)} {idn}")
                idn = ''
            print(f"OP_PRIDRUZI {(i + 1)} =")
            # dodaj za svaki operator da kad prije neg njega isprinta da
            # provjeri jel ima nes u idn ili u broj pa ak ima da prvo to
            # isprinta da se ne bi zezo kad su spojene operacije

        if retci[i][j] == '+':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            if idn != '':
                print(f"IDN {(i + 1)} {idn}")
                idn = ''
            print(f"OP_PLUS {(i + 1)} +")
        if retci[i][j] == '-':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            if idn != '':
                print(f"IDN {(i + 1)} {idn}")
                idn = ''
            print(f"OP_MINUS {(i + 1)} -")
        if retci[i][j] == '*':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            if idn != '':
                print(f"IDN {(i + 1)} {idn}")
                idn = ''
            print(f"OP_PUTA {(i + 1)} *")
        if retci[i][j] == '/':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            if idn != '':
                print(f"IDN {(i + 1)} {idn}")
                idn = ''
            print(f"OP_DIJELI {(i + 1)} /")

        if retci[i][j] == '(':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            if idn != '':
                print(f"IDN {(i + 1)} {idn}")
                idn = ''
            print(f"L_ZAGRADA {(i + 1)} (")

        if retci[i][j] == ')':
            if broj != '':
                print(f"BROJ {(i + 1)} {broj}")
                broj = ''
            if idn != '':
                print(f"IDN {(i + 1)} {idn}")
                idn = ''
            print(f"D_ZAGRADA {(i + 1)} )")
