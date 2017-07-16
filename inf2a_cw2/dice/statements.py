def add(item,lst):
    if (item not in lst):
        lst.insert(len(lst),item)

class Lexicon:
    """stores known word stems of various part-of-speech categories"""
    def __init__(self):
        self.dict = {}

    def add(self, stem, cat):
        if cat in self.dict.keys():
            self.dict[cat].append(stem)
        else:
            self.dict[cat] = [stem]

    def getAll(self, cat):
        return list(set(self.dict[cat]))

class FactBase:
    """stores unary and binary relational facts"""
    def __init__(self):
        self.dict = {}

    def addUnary(self, pred, e1):
        if pred in self.dict.keys():
            self.dict[pred].append(e1)
        else:
            self.dict[pred] = [e1]

    def queryUnary(self, pred, e1):
        if pred in self.dict:
            return e1 in self.dict[pred]
        return False;

    def addBinary(self, pred, e1, e2):
        if pred in self.dict.keys():
            self.dict[pred].append((e1, e2))
        else:
            self.dict[pred] = [(e1, e2)]

    def queryBinary(self, pred, e1, e2):
        if pred in self.dict:
            return (e1, e2) in self.dict[pred]
        return False;

import re

def verb_stem(s):
    """extracts the stem from the 3sg form of a verb, or returns empty string"""
    match = re.match(".*([^sxyzaeiou]|[^cs]h)s", s)
    if match:
        if match.group() == s:
            return s[:-1]

    match = re.match(".*[aeiou]ys", s)
    if match:
        if match.group() == s:
            return s[:-1]

    match = re.match(".+[^aeiou]ies", s)
    if match:
        if match.group() == s:
            return s[:-3] + 'y'

    match = re.match("[^aeiou]ies", s)
    if match:
        if match.group() == s:
            return s[:-1]

    match = re.match(".*(o|x|ch|sh|ss|zz)es", s)
    if match:
        if match.group() == s:
            return s[:-2]

    match = re.match(".*([^z]ze|[^s]se)s", s)
    if match:
        if match.group() == s:
            return s[:-1]

    match = re.match("has", s)
    if match:
        if match.group() == s:
            return "have"

    match = re.match(".*([^iosxz]|[^cs]h)es", s)
    if match:
        if match.group() == s:
            return s[:-1]

    return ''

def add_proper_name (w,lx):
    """adds a name to a lexicon, checking if first letter is uppercase"""
    if ('A' <= w[0] and w[0] <= 'Z'):
        lx.add(w,'P')
        return ''
    else:
        return (w + " isn't a proper name")


def process_statement (wlist,lx,fb):
    """analyses a statement and updates lexicon and fact base accordingly;
       returns '' if successful, or error message if not."""
    # Grammar for the statement language is:
    #   S  -> P is AR Ns | P is A | P Is | P Ts P
    #   AR -> a | an
    # We parse this in an ad hoc way.
    msg = add_proper_name (wlist[0],lx)
    if (msg == ''):
        if (wlist[1] == 'is'):
            if (wlist[2] in ['a','an']):
                lx.add (wlist[3],'N')
                fb.addUnary ('N_'+wlist[3],wlist[0])
            else:
                lx.add (wlist[2],'A')
                fb.addUnary ('A_'+wlist[2],wlist[0])
        else:
            stem = verb_stem(wlist[1])
            if (len(wlist) == 2):
                lx.add (stem,'I')
                fb.addUnary ('I_'+stem,wlist[0])
            else:
                msg = add_proper_name (wlist[2],lx)
                if (msg == ''):
                    lx.add (stem,'T')
                    fb.addBinary ('T_'+stem,wlist[0],wlist[2])
    return msg