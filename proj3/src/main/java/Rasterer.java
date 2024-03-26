import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // 图片的最大深度
    private static final int MAX_DEPTH_LEVEL = 8;
    // 返回的图片中左上角图片的x, y坐标
    private int startX;
    private int startY;
    // 返回图片中右下角图片的x, y坐标
    private int endX;
    private int endY;
    //
    private int lastX;
    private int lastY;


    // 这个类存放各深度的LonDPP和计算LonDPP所需的信息
    private class DepthInfoNode {
        private double ullon;
        private double ullat;
        private double lrlon;
        private double lrlat;
        private double LonDPP;
        // 在该层depth下，x每加1移动的距离，y每加1移动的距离
        private double stepEast;
        private double stepSouth;


        DepthInfoNode(double ullon, double ullat, double lrlon, double lrlat, double LonDPP) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlat = lrlat;
            this.lrlon = lrlon;
            this.LonDPP = LonDPP;
            stepEast = ullon - lrlon;
            stepSouth = ullat - lrlat;
        }

        public double getUllon() {
            return ullon;
        }

        public double getUllat() {
            return ullat;
        }

        public double getLrlon() {
            return lrlon;
        }

        public double getLrlat() {
            return lrlat;
        }

        public double getLonDPP() {
            return LonDPP;
        }

        public double getStepEast() {
            return stepEast;
        }

        public double getStepSouth() {
            return stepSouth;
        }
    }

    private DepthInfoNode[] depthInfo;

    public Rasterer() {
        // YOUR CODE HERE
        depthInfo = new DepthInfoNode[MAX_DEPTH_LEVEL];
        for (int i = 0; i < MAX_DEPTH_LEVEL; i++) {
            setDepthInfo(i);
        }
        startX = startY = endX = endY = 0;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
//        System.out.println(params);
        // 得到query box的相关数据
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double w = params.get("w");
        double h = params.get("h");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double requireLonDPP = getLonDPP(lrlon, ullon, w);
        // 应该发送的图片的深度等级
        int depth = getDepth(requireLonDPP);
        lastX = (int) Math.pow(2, depth) - 1;
        lastY = lastX;
        double stepEast = depthInfo[depth].getStepEast();
        double stepSouth = depthInfo[depth].getStepSouth();
        Map<String, Object> results = new HashMap<>();
        // 发送图片的起始坐标信息
        startX = getX(ullon, stepEast);
        startY = getY(ullat, stepSouth);
        endX = getX(lrlon, stepEast);
        endY = getY(lrlat, stepSouth);
        if (startX == -1 || startY == -1) {
            results.put("query_success", false);
            return results;
        }
        String[][] grid;
        if (endX == -1 || endY == -1) {
            grid = new String[lastY - startY + 1][lastX - startX + 1];
            int row = 0, col = 0;
            for (int i = startY; i <= lastY; i++) {
                for (int j = startX; j <= lastX; j++) {
                    String s = String.format("d%d_x%d_y%d.png", depth, j, i);
                    grid[row][col] = s;
                    col++;
                }
                row++;
                col = 0;
            }
        } else {
            grid = new String[endY - startY + 1][endX - startX + 1];
            int row = 0, col = 0;
            for (int i = startY; i <= endY; i++) {
                for (int j = startX; j <= endX; j++) {
                    String s = String.format("d%d_x%d_y%d.png", depth, j, i);
                    grid[row][col] = s;
                    col++;
                }
                row++;
                col = 0;
            }
        }
        double raster_ullon = MapServer.ROOT_ULLON + startX * stepEast;
        double raster_ullat = MapServer.ROOT_ULLAT - startY * stepSouth;
        double raster_lrlon = MapServer.ROOT_ULLAT + endX * stepEast;
        double raster_lrlat = MapServer.ROOT_ULLAT - endY * stepSouth;

        results.put("render_grid", grid);
        results.put("raster_ul_lon", raster_ullon);
        results.put("raster_ul_lat", raster_ullat);
        results.put("raster_lr_lon", raster_lrlon);
        results.put("raster_lr_lat", raster_lrlat);
        results.put("depth", depth);
        results.put("query_success", true);
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                + "your browser.");
        return results;
    }

    // 返回第一张跨越lon的图片的x坐标
    private int getX(double lon, double stepEast) {
        int x = 0;
        double curLon = MapServer.ROOT_ULLON;
        for (; curLon < MapServer.ROOT_LRLON; curLon += stepEast, x++) {
            if (curLon <= lon && curLon + stepEast > lon) {
                return x;
            }
        }
        return -1;
    }

    // 返回第一张跨越lat的图片的y坐标
    private int getY(double lat, double stepSouth) {
        int y = 0;
        double curLat = MapServer.ROOT_ULLAT;
        for (; curLat > MapServer.ROOT_LRLAT; curLat -= stepSouth, y++) {
            if (curLat >= lat && curLat - stepSouth < lat) {
                return y;
            }
        }
        return -1;
    }

    // 选择合适的图片深度等级
    private int getDepth(double requireLonDPP) {
        for (int i = 0; i < MAX_DEPTH_LEVEL; i++) {
            if (getLonDPPofDepth(i) <= requireLonDPP) {
                return i;
            }
        }
        return MAX_DEPTH_LEVEL - 1;
    }

    private double getLonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    // 计算第level层的图像的LonDPP
    private void setDepthInfo(int level) {
        if (level == 0) {
            double LonDPP = getLonDPP(MapServer.ROOT_LRLON, MapServer.ROOT_ULLON, MapServer.TILE_SIZE);
            DepthInfoNode node = new DepthInfoNode(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                    MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT, LonDPP);
            depthInfo[0] = node;
            return;
        }
        // 上一层深度的经纬度
        double prev_ullon = depthInfo[level - 1].getUllon();
        double prev_ullat = depthInfo[level - 1].getUllat();
        double prev_lrlon = depthInfo[level - 1].getLrlon();
        double prev_lrlat = depthInfo[level - 1].getLrlat();
        // 计算本层的经纬度和LonDPP
        double ullon = (prev_ullon + prev_lrlon) / 2;
        double ullat = (prev_ullat + prev_lrlat) / 2;
        double lrlon = prev_lrlon;
        double lrlat = prev_lrlat;
        double LonDPP = getLonDPP(lrlon, ullon, MapServer.TILE_SIZE);
        depthInfo[level] = new DepthInfoNode(ullon, ullat, lrlon, lrlat, LonDPP);
    }

    /* 得到第level层的图片的LonDPP */
    private double getLonDPPofDepth(int level) {
        if (level >= MAX_DEPTH_LEVEL) {
            throw new IllegalArgumentException("level must less than " + MAX_DEPTH_LEVEL);
        }
        return depthInfo[level].getLonDPP();
    }

    public static void main(String[] args) {
        // test LonDPP
        Rasterer r = new Rasterer();
        for (int i = 0; i < MAX_DEPTH_LEVEL; i++) {
            System.out.println("level" + i + "=" + r.getLonDPPofDepth(i));
        }
    }
}
