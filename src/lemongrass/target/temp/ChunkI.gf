
incomplete concrete ChunkI of Chunk = ClauseI, UtteranceI ** open Syntax in {

 lincat 

    Chunk_NP   = NP;
    Chunk_CN   = CN;
    Chunk_VP   = VP;
    Chunk_AP   = AP;
    Chunk_Adv  = Adv;
    Chunk_V2   = V2;
    Chunk_N2   = N2;
    Chunk_A2   = A2;
    Chunk_Prep = Prep;
    Chunk_Cl   = Cl;
    Chunk_Utt  = Utt;

 -- or:
 -- Chunk = { s : Str };

 lin 

    chunkEntity_NP     e = e; 

    chunkPredicate_VP  p = p;
    chunkPredicate_CN  p = p;
    chunkPredicate_AP  p = p;
    chunkPredicate_Adv p = p;

    chunkRelation_V2   r = r;
    chunkRelation_N2   r = r;
    chunkRelation_A2   r = r;
    chunkRelation_Prep r = r;

    chunkStatement_Cl  s = s;

    chunkUtterance_Utt u = u;
}