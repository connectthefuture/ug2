from statements import *
from pos_tagging import *
from agreement import *
from semantics import *

lx = Lexicon()
fb = FactBase()
s = 'John is a duck'.split()
r = 'Who is a duck ?'.split()
process_statement(s,lx,fb)
t = all_valid_parses(r,lx)[0]
t = restore_words(t,r)
lp = LogicParser()
l = lp.parse(sem(t))
l = l.simplify()