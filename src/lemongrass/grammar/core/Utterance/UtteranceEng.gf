
concrete UtteranceEng of Utterance = ClauseEng ** UtteranceI with (Syntax = SyntaxEng) ** open ExtraEng in {

 oper 

     negPol : Pol = UncNeg; -- for unconcatenated negation (e.g. "does not" instead of "doesn't")

}