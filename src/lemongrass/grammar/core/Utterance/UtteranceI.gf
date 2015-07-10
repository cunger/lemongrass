
incomplete concrete UtteranceI of Utterance = ClauseI ** open Syntax in {


 lincat

    Modality = VV;

    Sentence_S     = S;
    Question_QS    = QS;
    Imperative_Imp = Imp;
    Utterance_Utt  = Utt;

    WhPron = IP;
    WhAdv  = IAdv; 


 lin 

    ---- Modals ----

    MWant = want_VV;
    MCan  = can_VV;
    MKnow = can8know_VV;
    MMust = must_VV;

    modclause_VP  m p e = mkCl e m p;
    modclause_CN  m p e = mkCl e m (mkVP p);
    modclause_AP  m p e = mkCl e m (mkVP p);
    modclause_Adv m p e = mkCl e m (mkVP p);


    ---- Declarative sentences ----

    present_pos cl = mkS presentTense positivePol cl;
    present_neg cl = mkS presentTense negPol cl;
    past_pos    cl = mkS pastTense    positivePol cl;
    past_neg    cl = mkS pastTense    negPol cl;
    future_pos  cl = mkS futureTense  positivePol cl;
    future_neg  cl = mkS futureTense  negPol cl;

    ---- Coordination ----

    AndSentence  s1 s2 = mkS and_Conj s1 s2;
    OrSentence   s1 s2 = mkS or_Conj  s1 s2;


    ---- Imperative ----

    imperative p = mkImp p;


    ---- Questions ---- 

    Who   = who_IP;
    What  = what_IP;
    Where = where_IAdv;
    When  = when_IAdv;
    How   = how_IAdv;
    Why   = why_IAdv;

    WhichSg p = mkIP which_IDet    p; 
    WhichPl p = mkIP whichPl_IDet  p;
    HowMany p = mkIP how8many_IDet p;


    YesNo       s = mkQS s;
    queryAdv wh s = mkQS (mkQCl wh s);

    queryExt_V2   r wh e = mkQS (mkQCl wh (mkVP r e)); 
    queryExt_N2   r wh e = mkQS (mkQCl wh (mkVP (mkCN  r e))); 
    queryExt_A2   r wh e = mkQS (mkQCl wh (mkVP (mkAP  r e))); 
    queryExt_Prep r wh e = mkQS (mkQCl wh (mkVP (mkAdv r e))); 

    queryInt_V2   r e wh = mkQS (mkQCl wh (mkClSlash e (mkVPSlash r))); 
 -- queryInt_N2   r e wh = ? -- e.g. "e is the mother of whom?"
 -- queryInt_A2   r e wh = ? -- e.g. "e is similar to what?"
 -- queryInt_Prep r e wh = ? -- e.g. "e is above what?"

    queryPred_VP  wh p = mkQS (mkQCl wh p); 
    queryPred_CN  wh p = mkQS (mkQCl wh p); 
    queryPred_AP  wh p = mkQS (mkQCl wh p); 
    queryPred_Adv wh p = mkQS (mkQCl wh p); 

    queryCopulaWhPron wh e = mkQS (mkQCl (mkIComp wh) e); 
    queryCopulaWhAdv  wh e = mkQS (mkQCl (mkIComp wh) e);


    ---- Utterances ----

    yes = yes_Utt;
    no  = no_Utt;

    say_S   s = mkUtt s;
    say_NP  e = mkUtt e;
    say_VP  p = mkUtt p;
    say_CN  p = mkUtt p;
    say_AP  p = mkUtt p;
    say_Adv p = mkUtt p;

    ask_QS   q  = mkUtt q;
    ask_IP   wh = mkUtt wh;
    ask_IAdv wh = mkUtt wh;
    
    prompt i = mkUtt i;

}