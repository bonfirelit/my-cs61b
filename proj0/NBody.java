public class NBody{
    public static double readRadius(String filename) {
        In in = new In(filename);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        in.readDouble();
        Planet[] p = new Planet[n];
        for (int i = 0; i < n; i++) {
            p[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), 
                              in.readDouble(), in.readDouble(), in.readString());
        }
        return p;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]), dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        //draw background
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");

        StdDraw.enableDoubleBuffering();

        double[] xForces = new double[planets.length];
        double[] yForces = new double[planets.length];
        for (double time = 0; time <= T; time += dt) {
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
                planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                         planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                         planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }
    }
}