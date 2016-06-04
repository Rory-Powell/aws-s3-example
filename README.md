## AWS S3 basic example
Example code to upload and download a file from an S3 bucket

### Server
- spring-boot-starter
- aws-java-sdk-s3
- jdk8

---

### Instructions
- Create a bucket in S3
- In `config.properties`, add:
  - The bucket name
  - The desired directory in the bucket, leave blank for bucket root

Add the credentials for your IAM user to `.aws/credentials`
``` java
[default]
aws_access_key_id = Your access key id
aws_secret_access_key = Your secret access key
```
