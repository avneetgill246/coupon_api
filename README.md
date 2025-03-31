
COUPON API Assignment

Design
Implemented a factory design base with all coupon types having different subclass. Its similar to Spring security Authentication Provider where it checks different Authentication provider if they can handle the request. Implemented diff coupon type classes , each having applyCoupon method and isApplicable static method. isApplicable is made static as for checking each coupon to be applicable on a cart , creating a new object of coupontype from factory will not be efficient. Different subclasses for each coupon also helps in future extensibility and makes it easier to add new coupon types. 

All input edge cases
1. Validations on input fields (negative entries like threshold, discount, quantity, etc or discount percentage >100 or Invalid Coupon and Product Ids for applying coupon )(Added validatiopns in Dto but not handled throw exception)
2. Similar to validations is the discount percentage to be 0, so will be a coupon with no effect.(Not handled as a business decision)
3. For BxGy coupon, case of total get items in cart being less than max get items that we can get for free.	
4. Any validation of cart item prices sent by frontend be updated and correct. (Not Implemented)
 


Assumptions -:
1. Only 1 coupon can be applied at a time. (Cant add cart type discount on top of product type discount or multiple product type coupons)
2. If BxGy is valid but there are y+1 product which are from get array, will select random y products. (Preferably should be cheapest y products but simplified due to time constraint)
3. For applying Cart type coupon returning discount for each item as 0 but the total discount value in response will be present. 

