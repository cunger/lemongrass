
abstract Utterance = Clause ** {


 cat
  
    WhPron;
    WhAdv;

    Modality;

    Sentence_S;
    Question_QS;
    Imperative_Imp;

    Utterance_Utt;


 fun 


    ---- Modals ----

    MCan, MKnow, MMust, MWant : Modality;
    modclause_VP  : Modality -> Predicate_VP  -> Entity_NP -> Statement_Cl;
    modclause_CN  : Modality -> Predicate_CN  -> Entity_NP -> Statement_Cl;
    modclause_AP  : Modality -> Predicate_AP  -> Entity_NP -> Statement_Cl;
    modclause_Adv : Modality -> Predicate_Adv -> Entity_NP -> Statement_Cl;


    ---- Declarative sentences ----

    present_pos : Statement_Cl -> Sentence_S;
    present_neg : Statement_Cl -> Sentence_S;
    past_pos    : Statement_Cl -> Sentence_S;
    past_neg    : Statement_Cl -> Sentence_S;
    future_pos  : Statement_Cl -> Sentence_S;
    future_neg  : Statement_Cl -> Sentence_S;

    ---- Coordination 
 
    AndSentence, OrSentence : Sentence_S -> Sentence_S -> Sentence_S;


    ---- Imperative ----

    imperative : Predicate_VP -> Imperative_Imp;


    ---- Questions ----

    Who  : WhPron;
    What : WhPron;
    Where, When, How, Why : WhAdv;

    WhichSg, WhichPl, HowMany : Predicate_CN -> WhPron;

    YesNo    : Statement_Cl -> Question_QS;
    queryAdv : WhAdv -> Statement_Cl -> Question_QS;

    queryExt_V2   : Relation_V2   -> WhPron -> Entity_NP -> Question_QS;
    queryExt_N2   : Relation_N2   -> WhPron -> Entity_NP -> Question_QS;
    queryExt_A2   : Relation_A2   -> WhPron -> Entity_NP -> Question_QS;
    queryExt_Prep : Relation_Prep -> WhPron -> Entity_NP -> Question_QS;

    queryInt_V2   : Relation_V2   -> Entity_NP -> WhPron -> Question_QS;
    queryInt_N2   : Relation_N2   -> Entity_NP -> WhPron -> Question_QS;
    queryInt_A2   : Relation_A2   -> Entity_NP -> WhPron -> Question_QS;
    queryInt_Prep : Relation_Prep -> Entity_NP -> WhPron -> Question_QS;

    queryPred_VP  : WhPron -> Predicate_VP  -> Question_QS;
    queryPred_CN  : WhPron -> Predicate_CN  -> Question_QS;
    queryPred_AP  : WhPron -> Predicate_AP  -> Question_QS;
    queryPred_Adv : WhPron -> Predicate_Adv -> Question_QS;

    queryCopulaWhPron : WhPron -> Entity_NP -> Question_QS;
    queryCopulaWhAdv  : WhAdv  -> Entity_NP -> Question_QS;


    ---- Utterances ----

    yes : Utterance_Utt;
    no  : Utterance_Utt;

    say_S   : Sentence_S      -> Utterance_Utt;
    say_NP  : Entity_NP       -> Utterance_Utt;
    say_VP  : Predicate_VP    -> Utterance_Utt;
    say_CN  : Predicate_CN    -> Utterance_Utt;
    say_AP  : Predicate_AP    -> Utterance_Utt;
    say_Adv : Predicate_Adv   -> Utterance_Utt;

    ask_QS   : Question_QS    -> Utterance_Utt;
    ask_IP   : WhPron         -> Utterance_Utt;
    ask_IAdv : WhAdv          -> Utterance_Utt;
    
    prompt   : Imperative_Imp -> Utterance_Utt;

}
