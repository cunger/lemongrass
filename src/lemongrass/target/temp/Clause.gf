
abstract Clause = {

 cat

    Anaphor_Pron;

    Entity_NP;
   [Entity_NP] {2};

    Predicate_CN;
    Predicate_VP;
    Predicate_AP;
    Predicate_Adv;

    Relation_N2;
    Relation_V2;
    Relation_A2;
    Relation_Prep;

    Statement_Cl;


 fun 


    --------------------
    ---- Operations ----
    --------------------


    ---- Entity coordination 

    AndEntity, OrEntity : [Entity_NP] -> Entity_NP; 


    ---- Application 

    appl_V2_NP   : Relation_V2   -> Entity_NP -> Predicate_VP; 
    appl_N2_NP   : Relation_N2   -> Entity_NP -> Predicate_CN; 
    appl_A2_NP   : Relation_A2   -> Entity_NP -> Predicate_AP; 
    appl_Prep_NP : Relation_Prep -> Entity_NP -> Predicate_Adv;

    appl_CN_NP   : Predicate_CN  -> Entity_NP -> Statement_Cl; 
    appl_VP_NP   : Predicate_VP  -> Entity_NP -> Statement_Cl;
    appl_AP_NP   : Predicate_AP  -> Entity_NP -> Statement_Cl;
    appl_Adv_NP  : Predicate_Adv -> Entity_NP -> Statement_Cl;


    ---- Modification (first argument is the modifier, second one the modified)

    modify_AP_CN  : Predicate_AP  -> Predicate_CN -> Predicate_CN; 
    modify_Adv_VP : Predicate_Adv -> Predicate_VP -> Predicate_VP;


    ---------------------
    ---- Expressions ----
    ---------------------

    
    ---- Anaphors 

    I, We, YouSg, YouPl, He, She, It, They : Anaphor_Pron; 

    anaphor : Anaphor_Pron -> Entity_NP;
    poss    : Anaphor_Pron -> Predicate_CN -> Entity_NP;

    
    ---- Semantically light expressions 

    have_Rel    : Relation_V2;
    with_Rel    : Relation_Prep;
    possess_Rel : Relation_Prep;
    in_Rel      : Relation_Prep;
    from_Rel    : Relation_Prep;


    ---- Determiners 

    The, Some, All, No, Generic : Predicate_CN -> Entity_NP;

    -- -- Quantifiers in subject position (external argument)
    --
    -- SomeExt_CN_CN, AllExt_CN_CN, NoExt_CN_CN, GenericExt_CN_CN : Predicate_CN -> Predicate_CN -> Statement_Cl; 
    -- SomeExt_CN_VP, AllExt_CN_VP, NoExt_CN_VP, GenericExt_CN_VP : Predicate_CN -> Predicate_VP -> Statement_Cl; 
    -- SomeExt_CN_AP, AllExt_CN_AP, NoExt_CN_AP, GenericExt_CN_AP : Predicate_CN -> Predicate_AP -> Statement_Cl; 
    --
    -- -- Quantifiers in object  position (internal argument)
    -- 
    -- SomeInt_CN_V2, AllInt_CN_V2, NoInt_CN_V2, GenericInt_CN_V2 : Predicate_CN -> Relation_V2  -> Predicate_VP; 
    -- SomeInt_CN_N2, AllInt_CN_N2, NoInt_CN_N2, GenericInt_CN_N2 : Predicate_CN -> Relation_N2  -> Predicate_CN; 
    -- SomeInt_CN_A2, AllInt_CN_A2, NoInt_CN_A2, GenericInt_CN_A2 : Predicate_CN -> Relation_A2  -> Predicate_AP; 


    ---- Placeholders for NER 

    Entity1, Entity2, Entity3, Entity4, Entity5, Entity6, Entity7, Entity8, Entity9 : Entity_NP;

}