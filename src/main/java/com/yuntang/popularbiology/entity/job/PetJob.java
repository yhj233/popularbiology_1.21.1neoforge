package com.yuntang.popularbiology.entity.job;

import com.yuntang.popularbiology.entity.ai.mannager.ArcherJobMannager;
import com.yuntang.popularbiology.entity.ai.mannager.FarmerJobMannager;
import com.yuntang.popularbiology.entity.ai.mannager.FencerJobMannager;
import com.yuntang.popularbiology.entity.ai.mannager.NoneJobMannager;
import com.yuntang.popularbiology.entity.job.api.BasicJob;
import com.yuntang.popularbiology.entity.job.api.IPetJob;
import com.yuntang.popularbiology.entity.job.api.NoneJob;
import com.yuntang.popularbiology.init.InitTag;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum PetJob {
   NONE(0),
   FARMER(1),
   FENCER(2),
   ARCHER(3);

   private final int id;
   public static final Map<Integer, IPetJob> JOB_MAP = new HashMap(Map.of(0, new NoneJob(NoneJobMannager::initBrain, NoneJobMannager::tickBrain), 1, new BasicJob(InitTag.ENTITY_FARMER_TOOLS, FarmerJobMannager::initBrain, FarmerJobMannager::tickBrain), 2, new BasicJob(InitTag.ENTITY_FENCER_TOOLS, FencerJobMannager::initBrain, FencerJobMannager::tickBrain), 3, new BasicJob(InitTag.ENTITY_ARCHER_TOOLS, ArcherJobMannager::initBrain, ArcherJobMannager::tickBrain)));

   private PetJob(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public static IPetJob getJobFromId(int id) {
      return (IPetJob)JOB_MAP.get(id);
   }

   public static int getIdFromJob(IPetJob job) {
      return (Integer)JOB_MAP.entrySet().stream().filter((entry) -> {
         return ((IPetJob)entry.getValue()).equals(job);
      }).map(Entry::getKey).findFirst().orElse(-1);
   }

   // $FF: synthetic method
   private static PetJob[] $values() {
      return new PetJob[]{NONE, FARMER, FENCER, ARCHER};
   }
}
