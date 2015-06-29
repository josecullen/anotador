package application;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Styles {
	public static Insets insets = new Insets(3);
	public static CornerRadii corner = new CornerRadii(3);
	public static BackgroundFill bgfillBlue = new BackgroundFill(javafx.scene.paint.Color.LIGHTBLUE, corner, insets);
	public static BackgroundFill bgfillRed = new BackgroundFill(javafx.scene.paint.Color.ORANGE, corner, insets);
	
	
	
	public static Background BK_BLUE = new Background(bgfillBlue);		
	public static Background BK_ORANGE = new Background(bgfillRed);
	
	
	public static Border getBorder(Color color){
		BorderStrokeStyle bss = new BorderStrokeStyle(null, null, null, 10, 0, null);
		BorderStroke bs = new BorderStroke(color, color, color, color, bss,bss,bss,bss,corner, BorderWidths.DEFAULT, insets);
		return new Border(bs);			
	}
	
}
