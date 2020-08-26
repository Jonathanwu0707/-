
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
  
public class Robot extends TimedRobot {
  Joystick joy1 = new Joystick(0);
  WPI_TalonFX frontLeftMotor = new WPI_TalonFX(19);
  WPI_TalonFX backLeftMotor = new WPI_TalonFX(21);
  WPI_TalonFX frontRightMotor = new WPI_TalonFX(20);
  WPI_TalonFX backRightMotor = new WPI_TalonFX(18); 

  // DifferentialDrive front = new DifferentialDrive(frontLeftMotor, backLeftMotor);
  // DifferentialDrive back = new DifferentialDrive(frontRightMotor, backRightMotor);
  
  WPI_TalonFX flywheel = new WPI_TalonFX(4);

  VictorSPX rightarm =new VictorSPX(2);
  VictorSPX leftarm =new VictorSPX(5);
  VictorSPX  transport =new VictorSPX(1);
 
  
  @Override
  public void robotInit() {  
    backLeftMotor.configFactoryDefault();
    backRightMotor.configFactoryDefault();

    frontLeftMotor.follow(backLeftMotor);
    frontRightMotor.follow(backRightMotor);

    backRightMotor.setInverted(false);
    backLeftMotor.setInverted(false);
    frontLeftMotor.setInverted(InvertType.FollowMaster);
    frontRightMotor.setInverted(InvertType.FollowMaster);

    backLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
    backRightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);

    backRightMotor.setSensorPhase(true);
    backLeftMotor.setSensorPhase(false);
/*----------------------------------------------------------------------------------------------------------------------------------------------*/
    flywheel.configFactoryDefault();//防範超出預期的行為
    // flywheel.configSelectedFeedbackSensor(WPI_TalonFXFeedbackDevice.IntegratedSensor,Constants.kPIDLoopIdx,Constants.kTimeoutMs);//配置感測器
    flywheel.setSensorPhase(true);//設置感測器方向
    flywheel.setInverted(false);//設置馬達轉向

    flywheel.configNominalOutputReverse(0, Constants.kTimeoutMs);
    flywheel.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    flywheel.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx); 
    flywheel.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
    flywheel.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
    flywheel.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
    flywheel.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
    
    /* Set acceleration and vcruise velocity - see documentation */
    flywheel.configMotionCruiseVelocity(500, Constants.kTimeoutMs);
		flywheel.configMotionAcceleration(500, Constants.kTimeoutMs);

		/* Zero the sensor once on robot boot up */
    flywheel.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

 
  }


  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
  
  }

  @Override
  public void teleopInit() {

    }
    
  @Override
  public void teleopPeriodic() {
    
    final double speed = joy1.getRawAxis(0)*0.5;
    final double turn = -joy1.getRawAxis(1)*0.5;  
    double slider = -1.0 * joy1.getRawAxis(3); /* right-side Y for Xbox360Gamepad */
    
    backLeftMotor.set(ControlMode.PercentOutput,speed+turn);
    backRightMotor.set(ControlMode.PercentOutput,speed-turn);
    SmartDashboard.putNumber("speed",speed);
    SmartDashboard.putNumber("turn",turn);
    
    if(joy1.getRawButton(3)){
    rightarm.set(ControlMode.PercentOutput,0.2);
    leftarm.set(ControlMode.PercentOutput,0.2);
    transport.set(ControlMode.PercentOutput,-0.2);
   }else{
     rightarm.set(ControlMode.PercentOutput, 0);
     leftarm.set(ControlMode.PercentOutput, 0);
     transport.set(ControlMode.PercentOutput, 0);
   }
    
    if (joy1.getRawButton(1)) {
      double targetPos = slider * 2048 * 10.0;
      flywheel.set(ControlMode.Velocity,targetPos*0.1);
       SmartDashboard.putNumber("targetpos", targetPos*0.1);
     }
    else{
      flywheel.set(ControlMode.PercentOutput, 0);
   }   
    
  }

  @Override
  public void testInit() {

  }

  @Override
  public void testPeriodic() {

  }
}

  
  