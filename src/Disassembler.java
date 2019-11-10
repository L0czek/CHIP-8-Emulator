
import java.util.ArrayList;
import java.util.Optional;

public class Disassembler {

    private ArrayList<InstructionFactoryInterface> factories;

    public Disassembler(ArrayList<InstructionFactoryInterface> factories_) {
        this.factories = factories_;
    }

    public Optional<Instruction> decodeInstruction(short opcode) {
        for (InstructionFactoryInterface factory : factories) {
            Optional<Instruction> decoded = factory.fromOpcode(opcode);
            if(decoded.isPresent()) {
                return decoded;
            }
        }
        return Optional.empty();
    }

    public String disassemble(byte data, int offset) {
        return String.format("#@ %04X %04X : %02X    # db 0x%X\n", offset, offset +0x200, data, data);
    }

    public String disassemble(short data, int offset) {
        Optional<Instruction> instr = decodeInstruction(data);
        if(instr.isPresent()) {
            return String.format("#@ %04X %04X : %02X %02X # %s", offset, offset+0x200, (data >> 8)&0xff, data & 0xff, instr.get().disassemble());
        } else {
            return disassemble((byte)((data>>8) & 0xff), offset) + disassemble((byte)(data & 0xff), offset+1);
        }
    }

    private ArrayList<Integer> nextBranches(short value, int ip) {
        for(InstructionFactoryInterface factory : factories) {
            Optional<Instruction> decoded = factory.fromOpcode(value);
            if(decoded.isPresent()) {
                Instruction instr = decoded.get();
                InstructionVisitor.NextIpVisitor visitor = new InstructionVisitor.NextIpVisitor(ip);
                instr.accept(visitor);
                return visitor.getIps();
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<Integer> getCodeCoverage(byte[] data) {
        ArrayList<Integer> codeIndexes = new ArrayList<>();
        ArrayList<Integer> toCheck = new ArrayList<>();
        toCheck.add(0x200);
        while(!toCheck.isEmpty()) {
            int index = toCheck.get(0) - 0x200;
            toCheck.remove(0);
            if(codeIndexes.indexOf(index) != -1) {
                continue;
            }
            if(index >= data.length) {
                break;
            }
            short opcode = (short)((((int)data[index] << 8) & 0xff00) | ((int)data[index+1] & 0xff));
            ArrayList<Integer> nextIps = nextBranches(opcode, index + 0x200);
            codeIndexes.add(index);
            toCheck.addAll(nextIps);
        }
        return codeIndexes;
    }

    public String disassemble(byte[] data, int offset) {
        StringBuilder result = new StringBuilder();
        ArrayList<Integer> codeCoverage = getCodeCoverage(data);
        for(int i=0; i < data.length; i += 2) {
            if(i + 1 == data.length) {
                result.append(disassemble(data[i], i));
                break;
            }
            if(codeCoverage.indexOf(i) != -1) {
                short opcode = (short)(((((int)data[i]) << 8) & 0xff00) | (((int)data[i+1])& 0xff));
                result.append(disassemble(opcode, i));
            } else {
                result.append(disassemble(data[i], i));
                result.append(disassemble(data[i+1], i+1));
            }
        }
        return result.toString();
    }

    public String disassemble(byte[] data) {
        return disassemble(data, 0);
    }
}
