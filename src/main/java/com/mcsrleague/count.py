import os

def countPath(path: str):
    count = 0
    if os.path.isfile(path) and path.endswith(".java"):
        count += countFile(path)
    elif os.path.isdir(path):
        for ext in os.listdir(path):
            count += countPath(os.path.join(path,ext))
    return count
def countFile(path: str):
    with open(path,"r") as theFile:
        count = len(theFile.readlines())
        theFile.close()
    return count

print(countPath("mslmod"))
input()