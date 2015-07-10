
incomplete concrete ClauseI of Clause = open Syntax in {


 lincat

    Anaphor_Pron  = Pron;

    Entity_NP     =  NP;
   [Entity_NP]    = [NP];

    Predicate_CN  = CN;
    Predicate_VP  = VP;
    Predicate_AP  = AP;
    Predicate_Adv = Adv;

    Relation_N2   = N2;
    Relation_V2   = V2; 
    Relation_A2   = A2;
    Relation_Prep = Prep;

    Statement_Cl  = Cl;
 

 lin   


    --------------------
    ---- Operations ----
    --------------------


    ---- Entity coordination 

    BaseEntity e1 e2 = mkListNP e1 e2; 
    ConsEntity e1 e2 = mkListNP e1 e2;  
    AndEntity  e = mkNP and_Conj e; 
    OrEntity   e = mkNP or_Conj  e;


    ---- Application 

    appl_V2_NP   r e = mkVP  r e;  
    appl_N2_NP   r e = mkCN  r e;  
    appl_A2_NP   r e = mkAP  r e;  
    appl_Prep_NP r e = mkAdv r e; 

    appl_V2_NP_NP   r e1 e2 = mkCl e1 (mkVP r e2); 
    appl_N2_NP_NP   r e1 e2 = mkCl e1 (mkVP (mkNP (mkCN r e2))); -- what determiner, definite or indefinite, depends on n
    appl_A2_NP_NP   r e1 e2 = mkCl e1 (mkVP (mkAP r e2)); 
    appl_Prep_NP_NP r e1 e2 = mkCl e1 (mkAdv r e2); 


    ---- Modification (first argument is the modifier, second one the modified)

    modify_AP_CN  mod p = mkCN mod p;  
    modify_Adv_VP mod p = mkVP p mod; 


    ---------------------
    ---- Expressions ----
    ---------------------

    
    ---- Anaphors 

    I     = i_Pron;
    We    = we_Pron;
    YouSg = youSg_Pron;
    YouPl = youPl_Pron;
    He    = he_Pron;
    She   = she_Pron;
    It    = it_Pron;
    They  = they_Pron; 

    anaphor a   = mkNP a;
    poss    a p = mkNP a p; 


    ---- Semantically light expressions

    have_Rel    = have_V2;
    with_Rel    = with_Prep;
    possess_Rel = possess_Prep;
    in_Rel      = in_Prep;
    from_Rel    = from_Prep;


    ---- Determiners

    The     p = mkNP the_Det p;
    Some    p = mkNP aSg_Det p;
    All     p = variants { mkNP all_Predet (mkNP aPl_Det p);
                           mkNP every_Det p };
    No      p = mkNP (mkDet no_Quant) p;
    Generic p = mkNP aPl_Det p;

    -- -- Quantifiers in subject position (external argument)
    -- 
    -- SomeExt_CN_CN    p1 p2 = mkCl (mkNP aSg_Det p1) (mkVP p2); 
    -- SomeExt_CN_VP    p1 p2 = mkCl (mkNP aSg_Det p1) p2;
    -- SomeExt_CN_AP    p1 p2 = mkCl (mkNP aSg_Det p1) (mkVP p2);
    --
    -- NoExt_CN_CN      p1 p2 = mkCl (mkNP (mkDet no_Quant) p1) (mkVP p2); 
    -- NoExt_CN_VP      p1 p2 = mkCl (mkNP (mkDet no_Quant) p1) p2;
    -- NoExt_CN_AP      p1 p2 = mkCl (mkNP (mkDet no_Quant) p1) (mkVP p2);
    --
    -- AllExt_CN_CN     p1 p2 = variants { mkCl (mkNP all_Predet (mkNP aPl_Det p1)) (mkVP p2); 
    --                                     mkCl (mkNP every_Det p1) (mkVP p2) };
    -- AllExt_CN_VP     p1 p2 = variants { mkCl (mkNP all_Predet (mkNP aPl_Det p1)) p2; 
    --                                     mkCl (mkNP every_Det p1) p2 };
    -- AllExt_CN_AP     p1 p2 = variants { mkCl (mkNP all_Predet (mkNP aPl_Det p1)) (mkVP p2); 
    --                                     mkCl (mkNP every_Det p1) (mkVP p2) };
    --
    -- GenericExt_CN_CN p1 p2 = mkCl (mkNP aPl_Det p1) (mkVP p2); 
    -- GenericExt_CN_VP p1 p2 = mkCl (mkNP aPl_Det p1) p2;
    -- GenericExt_CN_AP p1 p2 = mkCl (mkNP aPl_Det p1) (mkVP p2);
    --
    -- -- Quantifiers in object  position (internal argument)
    -- 
    -- SomeInt_CN_V2    p r  = mkVP r (mkNP aSg_Det p); 
    -- SomeInt_CN_N2    p r  = mkCN r (mkNP aSg_Det p);
    -- SomeInt_CN_A2    p r  = mkAP r (mkNP aSg_Det p);
    --
    -- NoInt_CN_V2      p r  = mkVP r (mkNP (mkDet no_Quant) p); 
    -- NoInt_CN_N2      p r  = mkCN r (mkNP (mkDet no_Quant) p);
    -- NoInt_CN_A2      p r  = mkAP r (mkNP (mkDet no_Quant) p);
    --
    -- AllInt_CN_V2     p r  = variants { mkVP r (mkNP all_Predet (mkNP aPl_Det p)); 
    --                                    mkVP r (mkNP every_Det p) };
    -- AllInt_CN_N2     p r  = variants { mkCN r (mkNP all_Predet (mkNP aPl_Det p)); 
    --                                    mkCN r (mkNP every_Det p) };
    -- AllInt_CN_A2     p r  = variants { mkAP r (mkNP all_Predet (mkNP aPl_Det p)); 
    --                                    mkAP r (mkNP every_Det p) };
    --
    -- GenericInt_CN_V2 p r  = mkVP r (mkNP aPl_Det p); 
    -- GenericInt_CN_N2 p r  = mkCN r (mkNP aPl_Det p);
    -- GenericInt_CN_A2 p r  = mkAP r (mkNP aPl_Det p);

}