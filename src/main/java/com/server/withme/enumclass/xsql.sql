select a.*, count(*) from amrp008p0 a where cano = '15324865' and prdt_crdt_cd ='21' and office='Biz' and amt >= 10000000 group by office order by office desc;
