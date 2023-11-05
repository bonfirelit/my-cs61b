public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	static final double G = 6.67e-11;

	public Planet(double xP, double yP, double xV, double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p) {
		return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos) + (yyPos - p.yyPos) * (yyPos - p.yyPos));
	}
	public double calcForceExertedBy(Planet p) {
		return Planet.G * mass * p.mass / Math.pow(calcDistance(p), 2);
	}
	public double calcForceExertedByX(Planet p) {
		return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
	}
	public double calcForceExertedByY(Planet p) {
		return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
	}
	public double calcNetForceExertedByX(Planet[] planets) {
		double net_force = 0;
		for (Planet p : planets) {
			if(!equals(p))
				net_force += calcForceExertedByX(p);
		}
		return net_force;
	}
	public double calcNetForceExertedByY(Planet[] planets) {
		double net_force = 0;
		for (Planet p : planets) {
			if(!equals(p))
				net_force += calcForceExertedByY(p);
		}
		return net_force;
	}
	public void update(double dt, double fX, double fY) {
		xxVel = xxVel + (fX / mass) * dt;
		yyVel = yyVel + (fY / mass) * dt;
		xxPos = xxPos + xxVel * dt;
		yyPos = yyPos + yyVel * dt;
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
	}
}