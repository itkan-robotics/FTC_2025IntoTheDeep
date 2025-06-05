package org.firstinspires.ftc.teamcode.base.subsystems;

import android.graphics.Color;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ColorSensorSubsystem extends SubsystemBase {
    ColorSensor color;

    public ColorSensorSubsystem(HardwareMap h, String name) {
        this.color = h.get(ColorSensor.class, name);
    }

    public String getColor() {
        String detectedColor = "";
        // Read raw sensor data

        int red = color.red();

        int green = color.green();

        int blue = color.blue();

        int alpha = color.alpha();



        // Normalize RGB values to 0-255 range (REV outputs ~0–800 depending on lighting)

        float scaleFactor = 255.0f / Math.max(Math.max(red, Math.max(green, blue)), 1); // prevent divide-by-zero

        int normR = (int)(red * scaleFactor);

        int normG = (int)(green * scaleFactor);

        int normB = (int)(blue * scaleFactor);



        // Convert to HSV

        float[] hsv = new float[3];

        Color.RGBToHSV(normR, normG, normB, hsv);



        float hue = hsv[0];        // 0–360

        float saturation = hsv[1]; // 0–1

        float value = hsv[2];      // 0–1



        // Detect color using hue with lighting-insensitive thresholds

        if (saturation < 0.2 || value < 0.2) {

            detectedColor = "None"; // likely black or gray

        } else if ((hue < 30 || hue > 330)) {

            detectedColor = "Red";

        } else if (hue >= 40 && hue <= 70) {

            detectedColor = "Yellow";

        } else if (hue >= 180 && hue <= 250) {

            detectedColor = "Blue";

        } else {

            detectedColor = "Unknown";

        }
        if(color.green()>200){
            detectedColor = "Yellow";
        }
        else if (color.blue()>200){
            detectedColor = "Blue";
        }
        else if(color.red()>200){
            detectedColor = "Red";
        }
        return detectedColor;
    }}
