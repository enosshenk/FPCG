package com.shenko.golfgame;

public class GolfClubs {
	
	public int D_Power = 500;
	public int D_Angle = 8;
	
	public int I1_Power = 408;
	public int I1_Angle = 10;
	
	public int I3_Power = 358;
	public int I3_Angle = 12;
	
	public int I5_Power = 297;
	public int I5_Angle = 15;
	
	public int I7_Power = 254;
	public int I7_Angle = 17;
	
	public int I9_Power = 238;
	public int I9_Angle = 20;
	
	public int SW_Power = 140;
	public int SW_Angle = 40;
	
	public int P_Power = 180;
	public int P_Angle = 0;
	
	public float YardFactor = 6.5f;
	
	public int D_PixelDistance = 1240;
	public int D_YardDistance = 263;
	
	public int I1_PixelDistance = 987;
	public int I1_YardDistance = 210;

	public int I3_PixelDistance = 940;
	public int I3_YardDistance = 200;
	
	public int I5_PixelDistance = 799;
	public int I5_YardDistance = 170;
	
	public int I7_PixelDistance = 705;
	public int I7_YardDistance = 150;
	
	public int I9_PixelDistance = 657;
	public int I9_YardDistance = 139;
	
	public int SW_PixelDistance = 476;
	public int SW_YardDistance = 101;
	
	public int P_PixelDistance = 256;
	public int P_YardDistance = 54;
	
	public int GetPower(String inClub)
	{
		if (inClub == "D")
		{
			return D_Power;
		}
		else if (inClub == "I1")
		{
			return I1_Power;
		}
		else if (inClub == "I3")
		{
			return I3_Power;
		}
		else if (inClub == "I5")
		{
			return I5_Power;
		}
		else if (inClub == "I7")
		{
			return I7_Power;
		}
		else if (inClub == "I9")
		{
			return I9_Power;
		}
		else if (inClub == "SW")
		{
			return SW_Power;
		}
		else if (inClub == "P")
		{
			return P_Power;
		}
		
		return 0;
	}
	
	public int GetAngle(String inClub)
	{
		if (inClub == "D")
		{
			return D_Angle;
		}
		else if (inClub == "I1")
		{
			return I1_Angle;
		}
		else if (inClub == "I3")
		{
			return I3_Angle;
		}
		else if (inClub == "I5")
		{
			return I5_Angle;
		}
		else if (inClub == "I7")
		{
			return I7_Angle;
		}
		else if (inClub == "I9")
		{
			return I9_Angle;
		}
		else if (inClub == "SW")
		{
			return SW_Angle;
		}
		else if (inClub == "P")
		{
			return P_Angle;
		}
		
		return 0;
	}
	
	public int GetClubDistance(String inClub)
	{
		if (inClub == "D")
		{
			return D_PixelDistance;
		}
		else if (inClub == "I1")
		{
			return I1_PixelDistance;
		}
		else if (inClub == "I3")
		{
			return I3_PixelDistance;
		}
		else if (inClub == "I5")
		{
			return I5_PixelDistance;
		}
		else if (inClub == "I7")
		{
			return I7_PixelDistance;
		}
		else if (inClub == "I9")
		{
			return I9_PixelDistance;
		}
		else if (inClub == "SW")
		{
			return SW_PixelDistance;
		}
		else if (inClub == "P")
		{
			return P_PixelDistance;
		}
		
		return 0;		
	}
	
}
