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
    temp = []
    lst = []
    for k,v in lx.dict.iteritems():
        if wd in v or noun_stem(wd) in v or verb_stem(wd) in v:
            temp.append(k)
    for (w,t) in tagged_function_words:
        if wd == w:
            temp.append(t)
            
    for t in temp:
        if t == 'N':
            if noun_stem(wd) == '':
                lst.append(t + 's')
            elif noun_stem(wd) == wd:
                lst.append(t + 's')
                lst.append(t + 'p')
            else:
                lst.append(t + 'p')
        elif t == 'I' or t == 'T':
            if verb_stem(wd) == '':
                lst.append(t + 'p')
            else:
                lst.append(t + 's')
        else:
            lst.append(t)

    return lst

def tag_words (wds,lx):
    """returns a list of all possible taggings for a list of words"""
    if (wds == []):
        return [[]]
    else:
        tag_first = tag_word (wds[0],lx)
        tag_rest = tag_words (wds[1:],lx)
        return [[fst] + rst for fst in tag_first for rst in tag_rest]
