package frc.robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class OI {
    private final GenericHID m_stick;
    private final GenericHID op;
    
    public OI() {
        m_stick = new Joystick(0);
        op = new Joystick(1);
    }
}