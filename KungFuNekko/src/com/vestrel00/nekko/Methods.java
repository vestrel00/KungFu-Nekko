package com.vestrel00.nekko;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;

public class Methods {
	
	public static int incrementIndex(int index, int maxIndex) {
		if (++index == maxIndex)
			return 0;
		else
			return index;
	}

	public static void randomColor(Color color, Random rand) {
		color.set((float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f, 1.0f);
	}

	public static void updateColor(Color color, Color targetColor,
			float colorSpeed) {
		if (color.r < targetColor.r && (color.r += colorSpeed) > targetColor.r)
			color.r = targetColor.r;
		else if (color.r > targetColor.r
				&& (color.r -= colorSpeed) < targetColor.r)
			color.r = targetColor.r;
		if (color.g < targetColor.g && (color.g += colorSpeed) > targetColor.g)
			color.g = targetColor.g;
		else if (color.g > targetColor.g
				&& (color.g -= colorSpeed) < targetColor.g)
			color.g = targetColor.g;
		if (color.b < targetColor.b && (color.b += colorSpeed) > targetColor.b)
			color.b = targetColor.b;
		else if (color.b > targetColor.b
				&& (color.b -= colorSpeed) < targetColor.b)
			color.b = targetColor.b;
		if (color.a < targetColor.a && (color.a += colorSpeed) > targetColor.a)
			color.a = targetColor.a;
		else if (color.a > targetColor.a
				&& (color.a -= colorSpeed) < targetColor.a)
			color.a = targetColor.a;
	}

}
