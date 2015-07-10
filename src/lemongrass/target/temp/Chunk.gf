
abstract Chunk = Clause, Utterance ** {

 cat 

    Chunk_NP;
    Chunk_CN;
    Chunk_VP;
    Chunk_AP;
    Chunk_Adv;
    Chunk_V2;
    Chunk_N2;
    Chunk_A2;
    Chunk_Prep;
    Chunk_Cl;
    Chunk_Utt;

 -- or: 
 -- Chunk;

 fun 

    chunkEntity_NP     : Entity_NP     -> Chunk_NP;

    chunkPredicate_VP  : Predicate_VP  -> Chunk_VP;
    chunkPredicate_CN  : Predicate_CN  -> Chunk_CN;
    chunkPredicate_AP  : Predicate_AP  -> Chunk_AP;
    chunkPredicate_Adv : Predicate_Adv -> Chunk_Adv;

    chunkRelation_V2   : Relation_V2   -> Chunk_V2;
    chunkRelation_N2   : Relation_N2   -> Chunk_N2;
    chunkRelation_A2   : Relation_A2   -> Chunk_A2;
    chunkRelation_Prep : Relation_Prep -> Chunk_Prep;

    chunkStatement_Cl  : Statement_Cl  -> Chunk_Cl;

    chunkUtterance_Utt : Utterance_Utt -> Chunk_Utt;

}