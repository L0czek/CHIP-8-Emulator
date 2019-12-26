
import java.util.ArrayList;
import java.util.Optional;

/**
 * class implementing CHIP-8 disassembler
 */
public class Disassembler {

    private ArrayList<InstructionFactoryInterface> factories;

    /**
     * create disassembler from instruction factories which take opcode
     * @param factories_ list of instruction
     */
    public Disassembler(ArrayList<InstructionFactoryInterface> factories_) {
        this.factories = factories_;
    }

    /**
     * takes 2 bytes and returns either and decoded instruction or empty object on error
     * @param opcode opcode to be decoded
     * @return decoded instruction or empty
     */
    public Optional<Instruction> decodeInstruction(short opcode) {
        for (InstructionFactoryInterface factory : factories) {
            Optional<Instruction> decoded = factory.fromOpcode(opcode);
            if(decoded.isPresent()) {
                return decoded;
            }
        }
        return Optional.empty();
    }

    /**
     * disassembles one byte to emit byte assembly meta instruction
     * @param data one byte from bytecode
     * @param offset corresponding offset in bytecode
     * @return disassembled string
     */
    public String disassemble(byte data, int offset) {
        return String.format("#@ %04X %04X : %02X    # db 0x%X\n", offset, offset +0x200, data, data);
    }

    /**
     * disassemble 2 bytes (short) to either valid instruction or 2 emit bytes meta instructions
     * @param data short to be disassembled
     * @param offset corresponding offset in bytecode
     * @return disassembled string
     */
    public String disassemble(short data, int offset) {
        Optional<Instruction> instr = decodeInstruction(data);
        if(instr.isPresent()) {
            return String.format("#@ %04X %04X : %02X %02X # %s", offset, offset+0x200, (data >> 8)&0xff, data & 0xff, instr.get().disassemble());
        } else {
            return disassemble((byte)((data>>8) & 0xff), offset) + disassemble((byte)(data & 0xff), offset+1);
        }
    }

    /**
     * predicts the next ip address from current instruction
     * @param value current opcode
     * @param ip current ip
     * @return array of possible next ip values
     */
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

    /**
     * generates array of addresses which can be accessed by cpu to be executed as instruction
     * @param data bytecode
     * @return array of addresses
     */
    public ArrayList<Integer> getCodeCoverage(byte[] data) {
        ArrayList<Integer> codeIndexes = new ArrayList<>();
        ArrayList<Integer> toCheck = new ArrayList<>();
        toCheck.add(0x200);
        while(!toCheck.isEmpty()) {
            int index = toCheck.get(0) - 0x200;
            toCheck.remove(0);
            if(codeIndexes.indexOf(index) != -1 || index < 0) {
                continue;
            }
            if(index + 1 >= data.length) {
                break;
            }
            short opcode = (short)((((int)data[index] << 8) & 0xff00) | ((int)data[index+1] & 0xff));
            ArrayList<Integer> nextIps = nextBranches(opcode, index + 0x200);
            codeIndexes.add(index);
            toCheck.addAll(nextIps);
        }
        return codeIndexes;
    }

    /**
     * disassembles bytes at offset
     * @param data bytecode to be disassembled
     * @param offset offset of this bytes in bytecode
     * @return disassembly string
     */
    public String disassemble(byte[] data, int offset) {
        StringBuilder result = new StringBuilder();
        ArrayList<Integer> codeCoverage = getCodeCoverage(data);
        for(int i=0; i < data.length; ++i) {
            if(i + 1 == data.length) {
                result.append(disassemble(data[i], i));
                break;
            }
            if(codeCoverage.indexOf(i) != -1) {
                short opcode = (short)(((((int)data[i]) << 8) & 0xff00) | (((int)data[i+1])& 0xff));
                result.append(disassemble(opcode, i));
                i += 1;
            } else {
                result.append(disassemble(data[i], i));
            }
        }
        return result.toString();
    }

    /**
     * disassemble whole bytecode
     * @param data bytecode to be disassembled
     * @return disassembly string
     */
    public String disassemble(byte[] data) {
        return disassemble(data, 0);
    }
}
