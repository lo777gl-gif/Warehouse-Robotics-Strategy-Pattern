/** A customer order waiting in the queue. */
public record Order(String id, String sku, int quantity, int arrivalMinute) { }
