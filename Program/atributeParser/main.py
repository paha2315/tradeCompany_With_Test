def getLineTabs(line: str) -> int:
    tabs: int = 0
    while line[tabs] == '\t':
        tabs += 1
    return tabs


def haveSubItems(lines: list, idx: int) -> bool:
    idx_next = idx + 1
    if idx_next >= len(lines):
        return False
    offset = getLineTabs(lines[idx])
    offset_next = getLineTabs(lines[idx_next])
    return offset_next > offset


def join(src1: dict, src2: dict) -> dict:
    result = src1.copy()
    for key, val in src2.items():
        if key not in result:
            result[key] = val
    return dict(sorted(result.items(), key=lambda a: a[0]))


def joinToDict(L: list) -> dict:
    result = dict()
    for item in L:
        if item[0] in result:
            result[item[0]] = join(item[1], result[item[0]])
        else:
            result[item[0]] = item[1]
    return dict(sorted(result.items(), key=lambda a: a[0]))


def getAllAttributes(dct):
    attributes = []
    getAllAttributesRec(dct, attributes)
    return joinToDict(attributes)


def getAllAttributesRec(dct, attributes):
    deep = 0
    for key, val in dct.items():
        if type(val) is dict:
            val = dict(sorted(val.items(), key=lambda item: item[0]))
            current_depth = getAllAttributesRec(val, attributes)
            if current_depth == 1:
                attributes.append([key, val])
            deep = max(deep, current_depth)
    return deep + 1


def ReadStructure(lines: list):
    names = []
    depth_list = []
    for i in range(len(lines)):
        if lines[i] == "\n":
            continue
        depth = getLineTabs(lines[i]) + 1
        names.append(lines[i][depth - 1:-1])
        depth_list.append(depth)
    del depth

    pqe_dict = []
    pqe = [""]
    for i in range(len(names)):
        if len(pqe) == depth_list[i]:
            pqe.append(names[i])
            pqe_dict.append(dict())
        else:
            idx_start = len(pqe) - 2
            idx_end = depth_list[i] - 2
            pqe_dict[idx_start][pqe[idx_start + 1]] = i
            for j in range(idx_start - 1, idx_end, -1):
                pqe_dict[j][pqe[j + 1]] = pqe_dict[j + 1]
            pqe = pqe[0:depth_list[i]]
            pqe_dict = pqe_dict[0:depth_list[i]]
            pqe.append(names[i])

    i = len(names) - 1
    idx_start = len(pqe) - 2
    idx_end = depth_list[i] - 2
    pqe_dict[idx_start][pqe[idx_start + 1]] = i + 1
    for j in range(idx_start - 1, idx_end, -1):
        pqe_dict[j][pqe[j + 1]] = pqe_dict[j + 1]
    pqe = pqe[0:depth_list[i]]
    pqe_dict = pqe_dict[0:depth_list[i]]
    pqe.append(names[i])

    pqe = pqe[:-1]
    pqe_dict[0][pqe[1]] = pqe_dict[1]
    pqe_dict = pqe_dict[:-1]

    return getAllAttributes(pqe_dict[0]), pqe_dict[0]


def main():
    file_lines: list
    with open("atr.txt", encoding="UTF-8") as file:
        file_lines = [x for x in file.readlines() if len(x) != 0]
    del file
    file_lines[len(file_lines) - 1] += '\n'

    _, structure = ReadStructure(file_lines)
    del _, file_lines

    similar_doc_attributes = dict()
    for document, doc_attrs in structure.items():
        for attribute in doc_attrs:
            if type(doc_attrs[attribute]) is dict:
                if attribute in similar_doc_attributes:
                    similar_doc_attributes[attribute] = join(similar_doc_attributes[attribute], doc_attrs[attribute])
                else:
                    similar_doc_attributes[attribute] = doc_attrs[attribute]
    del attribute, document, doc_attrs

    for key, val in similar_doc_attributes.items():
        print(f"{key}")
        for key_, val_ in val.items():
            print(f"\t{key_} : {val_}")
    del key_, val_, key, val

    g = 4  # Сюда я ставлю точку останова, чтобы посмотреть результат
    pass


if __name__ == '__main__':
    main()
