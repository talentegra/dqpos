package com.pos.printer;

public class PrinterFunctionsLAN {
	private static LanPP LAN_PP = new LanPP();
	
	//--------------------------
	public static int PortDiscovery(String portName,int portSettings) {
		return LAN_PP.PortDiscovery(portName, portSettings);
	}
	
	public static int OpenPort(String portName,int portSettings) {
		return LAN_PP.OpenPort(portName, portSettings);
	}
	
	public static int ClosePort(String portName) {
		return LAN_PP.ClosePort(portName);
	}
	
	public static int PrintPDF417Code(String portName,int portSettings,int Columns,int Rows,int Width,int Height,int CorrectionLevel,String dArray) {
		return LAN_PP.PrintPDF417Code(portName, portSettings, Columns, Rows, Width, Height, CorrectionLevel, dArray);
	}
	
	public static int PrintQrCode(String portName,int portSettings,int n,int mod,int size,int version,String dArray) {
		return LAN_PP.PrintQrCode(portName, portSettings, n, mod, size, version, dArray);
	}
	
	public static int OpenCashDrawer(String portName,int portSettings,int m,int t) {
		return LAN_PP.OpenCashDrawer(portName, portSettings, m, t);
	}
	
	public static int CheckStatus(String portName,int portSettings,int n) {
		return LAN_PP.CheckStatus(portName, portSettings, n);
	}
	
	public static int PrintCode39(String portName,int portSettings,String barcodeData,int option,int height,int width) {
		return LAN_PP.PrintCode39(portName, portSettings, barcodeData, option, height, width);
	}
	
	public static int PrintCode93(String portName,int portSettings,String barcodeData,int option,int height,int width) {
		return LAN_PP.PrintCode93(portName, portSettings, barcodeData, option, height, width);
	}
	
	public static int PrintCodeITF(String portName,int portSettings,String barcodeData,int option,int height,int width) {
		return LAN_PP.PrintCodeITF(portName, portSettings, barcodeData, option, height, width);
	}
	
	public static int PrintCode128(String portName,int portSettings,String barcodeData,int option,int height,int width) {
		return LAN_PP.PrintCode128(portName, portSettings, barcodeData, option, height, width);
	}
	
	public static int PreformCut(String portName,int portSettings,int cuttype) {
		return LAN_PP.PreformCut(portName,portSettings,cuttype);
	}
	
	public static int PrintText(String portName,int portSettings,int underline,int invertColor,int emphasized,int upsideDown,int heightExpansion,int widthExpansion,int leftMargin,int alignment,String textData) {
		return LAN_PP.PrintText(portName, portSettings, underline, invertColor, emphasized, upsideDown, heightExpansion, widthExpansion, leftMargin, alignment, textData);
	}
	
	public static int PrintTextKanji(String portName,int portSettings,int underline,int invertColor,int emphasized,int upsideDown,int heightExpansion,int widthExpansion,int leftMargin,int alignment,byte[] textData) {
		return LAN_PP.PrintTextKanji(portName, portSettings, underline, invertColor, emphasized, upsideDown, heightExpansion, widthExpansion, leftMargin, alignment, textData);
	}
	
	public static int PrintBitmapImage(String portName,int portSettings,int m,String ImgUrl) {
		return LAN_PP.PrintBitmapImage(portName, portSettings, m, ImgUrl);
	}
	
	public static int PrintSampleReceipt(String portName,int portSettings) {
		return LAN_PP.PrintSampleReceipt(portName, portSettings);
	}
	
	public static int PrintSampleReceiptCn(String portName,int portSettings) {
		return LAN_PP.PrintSampleReceiptCn(portName, portSettings);
	}
	
	//--------------------------
	public static int PrintBarCode(String portName,int portSettings,String barcodeData,int option,int height,int width,int alignment,int mod) {
		return LAN_PP.PrintBarCode(portName, portSettings, barcodeData, option, height, width, alignment, mod);
	}
	
	public static int DefineNVBitImage(String portName,int portSettings,String ImgUrl) {
		return LAN_PP.DefineNVBitImage(portName, portSettings, ImgUrl);
	}
	
	public static int DefineNVBitImageTwo(String portName,int portSettings,String ImgUrl1,String ImgUrl2) {
		return LAN_PP.DefineNVBitImageTwo(portName, portSettings, ImgUrl1, ImgUrl2);
	}
	
	public static int PrintNVBitImage(String portName,int portSettings,int n,int m) {
		return LAN_PP.PrintNVBitImage(portName, portSettings, n, m);
	}
	
	public static int SelectPrintMode(String portName,int portSettings,int mod) {
		return LAN_PP.SelectPrintMode(portName, portSettings, mod);
	}
	
	public static int CancelPrintDataInPageMode(String portName,int portSettings) {
		return LAN_PP.CancelPrintDataInPageMode(portName, portSettings);
	}
	
	public static int SelectPrintDirectionInPageMode(String portName,int portSettings,int n) {
		return LAN_PP.SelectPrintDirectionInPageMode(portName, portSettings, n);
	}
	
	public static int SetPrintingAreaInPageMode(String portName,int portSettings,int X,int Y,int Width,int Height) {
		return LAN_PP.SetPrintingAreaInPageMode(portName, portSettings, X, Y, Width, Height);
	}
	
	public static int SetPrintPositionInPageMode(String portName,int portSettings,int p) {
		return LAN_PP.SetPrintPositionInPageMode(portName, portSettings, p);
	}
	
	public static int PrintDataInPageMode(String portName,int portSettings) {
		return LAN_PP.PrintDataInPageMode(portName, portSettings);
	}
	
	public static int SetLineSpacing(String portName,int portSettings,int n) {
		return LAN_PP.SetLineSpacing(portName, portSettings, n);
	}
	
	public static int SelectCharacterFont(String portName,int portSettings,int n) {
		return LAN_PP.SelectCharacterFont(portName, portSettings, n);
	}
	
	public static int SelectCodePage(String portName,int portSettings,String CodePagName) {
		return LAN_PP.SelectCodePage(portName, portSettings, CodePagName);	
	}
	
	public static int SelectInternationalCharacter(String portName,int portSettings,String CharName) {
		return LAN_PP.SelectInternationalCharacter(portName, portSettings, CharName);
	}
}
