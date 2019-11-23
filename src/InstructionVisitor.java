import java.util.ArrayList;

public class InstructionVisitor {
    interface Visitor {
        void visit(InstructionSet.Call instr);
        void visit(InstructionSet.Return instr);
        void visit(InstructionSet.Jump instr);
        void visit(InstructionSet.CallWordPtr instr);
        void visit(InstructionSet.SkipEqualImm instr);
        void visit(InstructionSet.SkipNotEqualImm instr);
        void visit(InstructionSet.SkipEqualReg instr);
        void visit(InstructionSet.LoadImm instr);
        void visit(InstructionSet.AddImm instr);
        void visit(InstructionSet.Mov instr);
        void visit(InstructionSet.Or instr);
        void visit(InstructionSet.And instr);
        void visit(InstructionSet.Xor instr);
        void visit(InstructionSet.Add instr);
        void visit(InstructionSet.Sub instr);
        void visit(InstructionSet.RShift1 instr);
        void visit(InstructionSet.SubR instr);
        void visit(InstructionSet.LShift1 instr);
        void visit(InstructionSet.SkipNotEqualReg instr);
        void visit(InstructionSet.LoadRegI instr);
        void visit(InstructionSet.BranchRelv0 instr);
        void visit(InstructionSet.Rand instr);
        void visit(InstructionSet.DisplayClear instr);
        void visit(InstructionSet.GetDelayTimerCounter instr);
        void visit(InstructionSet.SetDelayTimerCounter instr);
        void visit(InstructionSet.SetSoundTimerCounter instr);
        void visit(InstructionSet.AddRegI instr);
        void visit(InstructionSet.StoreBCD instr);
        void visit(InstructionSet.RegDump instr);
        void visit(InstructionSet.RegLoad instr);
        void visit(InstructionSet.DrawSprite instr);
        void visit(InstructionSet.SkipEqualKey skipEqualKey);
        void visit(InstructionSet.SkipNotEqualKey skipNotEqualKey);
        void visit(InstructionSet.GetKey getKey);
        void visit(InstructionSet.GetSpriteAddress getSpriteAddress);
    }

    public static class NextIpVisitor implements Visitor {
        ArrayList<Integer> ips;
        int currentIp;

        public NextIpVisitor(int currentIp) {
            ips = new ArrayList<>();
            this.currentIp = currentIp;
        }

        public ArrayList<Integer> getIps() {
            return ips;
        }

        @Override
        public void visit(InstructionSet.Call instr) {
            ips.add(currentIp + 2);
            ips.add(instr.getValueNNN());
        }

        @Override
        public void visit(InstructionSet.Return instr) {

        }

        @Override
        public void visit(InstructionSet.Jump instr) {
            ips.add(instr.getValueNNN());
        }

        @Override
        public void visit(InstructionSet.CallWordPtr instr) {
            ips.add(instr.getValueNNN());
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.SkipEqualImm instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }

        @Override
        public void visit(InstructionSet.SkipNotEqualImm instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }

        @Override
        public void visit(InstructionSet.SkipEqualReg instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }

        @Override
        public void visit(InstructionSet.LoadImm instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.AddImm instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.Mov instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.Or instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.And instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.Xor instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.Add instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.Sub instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.RShift1 instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.SubR instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.LShift1 instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.SkipNotEqualReg instr) {
            ips.add(currentIp+2);
            ips.add(currentIp+4);
        }

        @Override
        public void visit(InstructionSet.LoadRegI instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.BranchRelv0 instr) {

        }

        @Override
        public void visit(InstructionSet.Rand instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.DisplayClear instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.GetDelayTimerCounter instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.SetDelayTimerCounter instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.SetSoundTimerCounter instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.AddRegI instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.StoreBCD instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.RegDump instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.RegLoad instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.DrawSprite instr) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.SkipEqualKey skipEqualKey) {
            ips.add(currentIp + 2);
            ips.add(currentIp + 4);
        }

        @Override
        public void visit(InstructionSet.SkipNotEqualKey skipNotEqualKey) {
            ips.add(currentIp + 2);
            ips.add(currentIp + 4);
        }

        @Override
        public void visit(InstructionSet.GetKey getKey) {
            ips.add(currentIp + 2);
        }

        @Override
        public void visit(InstructionSet.GetSpriteAddress getSpriteAddress) {
            ips.add(currentIp + 2);
        }
    }
}
