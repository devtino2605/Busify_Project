### Running with Docker

In order to run the api image without any errors, you need to provide the following environment variables:

```yml
  environment:
    - "DB_URL=jdbc:mysql://localhost:3306/busify?createDatabaseIfNotExist=true"
    - "DB_USER=root"
    - "DB_PASSWORD=1111"
    - "MONGO_URI=mongodb://localhost:27017/busify"
    - "MONGO_DATABASE=busify"
    - "JWT_SECRET="
    - "JWT_EXPIRATION=86400000"
    - "JWT_REFRESH_EXPIRATION=604800000"
    - "PAYPAL_CLIENT_ID="
    - "PAYPAL_CLIENT_SECRET="
    - "PAYPAL_MODE=sandbox"
    - "VNPAY_MERCHANT_CODE="
    - "VNPAY_SECRET_KEY="
    - "VNPAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"
    - "VNPAY_REFUND_URL=https://sandbox.vnpayment.vn/merchant_webapi/api/transaction"
    - "VNPAY_RETURN_URL=http://localhost:3000/payment/callback"
    - "GOOGLE_CLIENT_ID="
    - "GOOGLE_CLIENT_SECRET="
    - "GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google"
    - "MAIL_USERNAME="
    - "MAIL_PASSWORD="
    - "FRONTEND_URL=http://localhost:3000"
    - "CLOUDINARY_CLOUD_NAME="
    - "CLOUDINARY_API_KEY="
    - "CLOUDINARY_API_SECRET="
    - "REDIS_HOST=localhost"
    - "REDIS_PORT=6379"
    - "SERVER_PORT=8080"
    - "OPENAI_API_KEY="
```