import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Disassembler {

    private ArrayList<InstructionFactoryInterface> factories;

    public Disassembler(ArrayList<InstructionFactoryInterface> factories_) {
        this.factories = factories_;
    }

    public Optional<Instruction> decodeInstruction(short opcode) {
        for (var factory : factories) {
            var decoded = factory.fromOpcode(opcode);
            if(decoded.isPresent()) {
                return decoded;
            }
        }
        return Optional.empty();
    }

    public String tryDisassemble(short opcode, int address) {
        var decodedInstruction = decodeInstruction(opcode);
        byte msb = (byte)(opcode >> 8);
        byte lsb = (byte)(opcode & 0xff);
        if(decodedInstruction.isPresent()) {
            return String.format("# %04X: %02X %02X # %s", address, msb, lsb, decodedInstruction.get().disassemble());
        } else {
            return String.format("# %04X: %02X %02X # db 0x%X, 0x%X", address, msb, lsb, msb, lsb);
        }
    }

    public String tryDisassemble(byte[] byteCode) {
        StringBuilder result = new StringBuilder(new String("# RVA   VA  : Bytes | Instruction #\n"));
        int i=1;
        for(; i < byteCode.length; ++i) {
            byte msb = byteCode[i-1];
            byte lsb = byteCode[i];
            short maybeOpcode = (short)((msb & 0xff) << 8 | (lsb & 0xff));
            var decodedInstruction = decodeInstruction(maybeOpcode);
            if(decodedInstruction.isPresent()) {
                result.append(String.format("# %04X  %04X: %02X %02X # %s", i - 1, i-1+0x200, msb & 0xff, lsb & 0xff, decodedInstruction.get().disassemble()));
                i += 1;
            } else {
                result.append(String.format("# %04X  %04X: %02X    # db 0x%X\n", i-1, i-1 + 0x200, msb & 0xff, msb & 0xff));
            }
        }
        if(byteCode.length == i) {
            var last = byteCode[byteCode.length - 1];
            result.append(String.format("# %04X  %04X: %02X    # db 0x%X\n", byteCode.length, byteCode.length + 0x200, last & 0xff, last & 0xff));
        }
        return result.toString();
    }

}
