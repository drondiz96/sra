FROM nginx:stable-alpine

# Install openssl to generate self-signed certificates
RUN apk add --no-cache openssl

# Generate a self-signed certificate for reviewphoneserve
RUN mkdir -p /etc/nginx/ssl \
    && openssl req -x509 -nodes -days 365 \
        -newkey rsa:2048 \
        -keyout /etc/nginx/ssl/selfsigned.key \
        -out /etc/nginx/ssl/selfsigned.crt \
        -subj "/CN=reviewphoneserve"

# Copy Nginx configuration
COPY nginx-grafana.conf /etc/nginx/conf.d/default.conf

# Expose HTTP and HTTPS ports
EXPOSE 80 443

# Run Nginx in the foreground
ENTRYPOINT ["nginx", "-g", "daemon off;"]