from statements import *

tagged_function_words = [('a','AR'), ('an','AR'), ('and','AND'),
     ('is','BEs'), ('are','BEp'), ('does','DOs'), ('do','DOp'),
     ('who','WHO'), ('which','WHICH'), ('Who','WHO'), ('Which','WHICH'), ('?','?')]
     # upper or lowercase tolerated at start of question.

function_words = [p[0] for p in tagged_function_words]

# English nouns with identical plural forms (list courtesy of Wikipedia):

identical_plurals = ['bison','buffalo','deer','fish','moose','pike','plankton',
     'salmon','sheep','swine','trout']


def noun_stem (s):
    """extracts the stem from a plural noun, or returns empty string"""
    if s in identical_plurals:
        return s
    elif s[-3:] == "man":
        return s[:-2] + "en"
    else:
        return verb_stem(s)

def tag_word (wd,lx):
    """returns a list of all possible tags for wd relative to lx"""
    lst = []

    for w,tag in tagged_function_words:
        if wd == w:
            lst.append(tag)

    for k,v in lx.dict.iteritems():
        if k == 'N':
            if wd in identical_plurals:
                lst.append('Ns')
                lst.append('Np')
            elif wd in v:
                lst.append('Ns')
            elif noun_stem(wd) in v:
                lst.append('Np')
        elif k == 'I' or k == 'T':
            if wd in v:
                lst.append(k + 'p')
            elif verb_stem(wd) in v:
                lst.append(k + 's')
        else:
            if wd in v:
                lst.append(k)

    return lst

def tag_words (wds,lx):
    """returns a list of all possible taggings for a list of words"""
    if (wds == []):
        return [[]]
    else:
        tag_first = tag_word (wds[0],lx)
        tag_rest = tag_words (wds[1:],lx)
        return [[fst] + rst for fst in tag_first for rst in tag_rest]
