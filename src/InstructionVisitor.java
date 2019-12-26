import java.util.ArrayList;

/**
 * class being placeholder for instruction visitors
 */
public class InstructionVisitor {
    interface Visitor {

        /**
         * implementation for Call instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Call instr);

        /**
         * implementation for Return instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Return instr);

        /**
         * implementation for Jump instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Jump instr);

        /**
         * implementation for CallWordPtr instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.CallWordPtr instr);

        /**
         * implementation for SkipEqualImm instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SkipEqualImm instr);

        /**
         * implementation for SkipNotEqualImm instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SkipNotEqualImm instr);

        /**
         * implementation for SkipEqualReg instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SkipEqualReg instr);

        /**
         * implementation for LoadImm instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.LoadImm instr);

        /**
         * implementation for AddImm instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.AddImm instr);

        /**
         * implementation for Mov instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Mov instr);

        /**
         * implementation for Or instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Or instr);

        /**
         * implementation for And instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.And instr);

        /**
         * implementation for Xor instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Xor instr);

        /**
         * implementation for Add instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Add instr);

        /**
         * implementation for Sub instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Sub instr);

        /**
         * implementation for RShift1 instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.RShift1 instr);

        /**
         * implementation for SubR instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SubR instr);

        /**
         * implementation for LShift1 instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.LShift1 instr);

        /**
         * implementation for SkipNotEqualReg instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SkipNotEqualReg instr);

        /**
         * implementation for LoadRegI instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.LoadRegI instr);

        /**
         * implementation for BranchRelv0 instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.BranchRelv0 instr);

        /**
         * implementation for Rand instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.Rand instr);

        /**
         * implementation for DisplayClear instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.DisplayClear instr);

        /**
         * implementation for GetDelayTimerCounter instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.GetDelayTimerCounter instr);

        /**
         * implementation for SetDelayTimerCounter instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SetDelayTimerCounter instr);

        /**
         * implementation for SetSoundTimerCounter instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SetSoundTimerCounter instr);

        /**
         * implementation for AddRegI instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.AddRegI instr);

        /**
         * implementation for StoreBCD instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.StoreBCD instr);

        /**
         * implementation for RegDump instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.RegDump instr);

        /**
         * implementation for RegLoad instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.RegLoad instr);

        /**
         * implementation for DrawSprite instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.DrawSprite instr);

        /**
         * implementation for SkipEqualKey instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SkipEqualKey skipEqualKey);

        /**
         * implementation for SkipNotEqualKey instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.SkipNotEqualKey skipNotEqualKey);

        /**
         * implementation for GetKey instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.GetKey getKey);

        /**
         * implementation for GetSpriteAddress instruction
         * @param instr instruction object visited
         */
        void visit(InstructionSet.GetSpriteAddress getSpriteAddress);
    }

    /**
     * concrete visitor predicting next values of ip
     */
    public static class NextIpVisitor implements Visitor {
        ArrayList<Integer> ips;
        int currentIp;

        /**
         * construct from current ip
         * @param currentIp current ip register value
         */
        public NextIpVisitor(int currentIp) {
            ips = new ArrayList<>();
            this.currentIp = currentIp;
        }

        /**
         * return result
         * @return array of next possible ip values
         */
        public ArrayList<Integer> getIps() {
            return ips;
        }

        /**
         * visiting Call instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Call instr) {
            ips.add(currentIp + 2);
            ips.add(instr.getValueNNN());
        }



        /**
         * visiting Return instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Return instr) {

        }



        /**
         * visiting Jump instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Jump instr) {
            ips.add(instr.getValueNNN());
        }



        /**
         * visiting CallWordPtr instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.CallWordPtr instr) {
            ips.add(instr.getValueNNN());
            ips.add(currentIp + 2);
        }



        /**
         * visiting SkipEqualImm instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SkipEqualImm instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }



        /**
         * visiting SkipNotEqualImm instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SkipNotEqualImm instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }



        /**
         * visiting SkipEqualReg instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SkipEqualReg instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }



        /**
         * visiting LoadImm instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.LoadImm instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting AddImm instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.AddImm instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting Mov instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Mov instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting Or instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Or instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting And instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.And instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting Xor instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Xor instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting Add instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Add instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting Sub instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Sub instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting RShift1 instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.RShift1 instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting SubR instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SubR instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting LShift1 instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.LShift1 instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting SkipNotEqualReg instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SkipNotEqualReg instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }



        /**
         * visiting LoadRegI instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.LoadRegI instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting BranchRelv0 instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.BranchRelv0 instr) {

        }



        /**
         * visiting Rand instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.Rand instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting DisplayClear instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.DisplayClear instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting GetDelayTimerCounter instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.GetDelayTimerCounter instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting SetDelayTimerCounter instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SetDelayTimerCounter instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting SetSoundTimerCounter instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SetSoundTimerCounter instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting AddRegI instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.AddRegI instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting StoreBCD instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.StoreBCD instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting RegDump instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.RegDump instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting RegLoad instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.RegLoad instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting DrawSprite instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.DrawSprite instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting SkipEqualKey instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SkipEqualKey instr) {
            ips.add(currentIp + 2);
            ips.add(currentIp + 4);
        }



        /**
         * visiting SkipNotEqualKey instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.SkipNotEqualKey instr) {
            ips.add(currentIp + 2);
            ips.add(currentIp + 4);
        }



        /**
         * visiting GetKey instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.GetKey instr) {
            ips.add(currentIp + 2);
        }



        /**
         * visiting GetSpriteAddress instruction to predict next ip value
         * @param instr instruction to be visited
         */
        @Override
        public void visit(InstructionSet.GetSpriteAddress instr) {
            ips.add(currentIp + 2);
        }


    }
}
