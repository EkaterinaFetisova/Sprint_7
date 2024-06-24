package example;

public class OrderAPI {
    public static final String getOrdersListAPIPath =  "/api/v1/orders";
    public static final String newOrderPath = "/api/v1/orders";

    public static String getOrderByIdPath(int orderId) {
        return "/api/v1/orders/track?t="+orderId;
    }

    public static String getFinishOrderPath(int orderId) {
        return "/api/v1/orders/finish/"+orderId;
    }
}