package com.amazonaws.samples.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Handwritten S3 client samples
 */
public class S3Samples {

    public static void main(String[] args) throws IOException {
        //BEGIN_SAMPLE:AmazonS3.CreateClient
        //TITLE:Create an S3 client
        //DESCRIPTION:Create your credentials file at ~/.aws/credentials (C:\Users\USER_NAME\.aws\credentials for Windows users)
        AmazonS3 s3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        //END_SAMPLE

        String bucketName = "my-first-s3-bucket-" + UUID.randomUUID();
        String key = "MyObjectKey";

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon S3");
        System.out.println("===========================================\n");

        try {
            System.out.println("Creating bucket " + bucketName + "\n");

            //BEGIN_SAMPLE:AmazonS3.CreateBucket
            //TITLE:Create an S3 bucket
            //DESCRIPTION:Amazon S3 bucket names are globally unique, so once a bucket name has been taken by any user, you can't create another bucket with that same name.
            s3.createBucket(bucketName);
            //END_SAMPLE


            System.out.println("Listing buckets");
            //BEGIN_SAMPLE:AmazonS3.ListBuckets
            //TITLE:List buckets
            //DESCRIPTION:List the buckets in your account
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(" - " + bucket.getName());
            }
            System.out.println();
            //END_SAMPLE


            System.out.println("Uploading a new object to S3 from a file\n");
            //BEGIN_SAMPLE:AmazonS3.PutObject
            //TITLE:Upload an object to a bucket
            //DESCRIPTION:You can easily upload a file to S3, or upload directly an InputStream if you know the length of the data in the stream.
            s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));
            //END_SAMPLE

            System.out.println("Downloading an object");
            //BEGIN_SAMPLE:AmazonS3.GetObject
            //TITLE:Download an S3 object.
            //DESCRIPTION:When you download an object, you get all of the object's metadata and a stream from which to read the contents.
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
            //END_SAMPLE
            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
            displayTextInputStream(object.getObjectContent());


            System.out.println("Listing objects");

            //BEGIN_SAMPLE:AmazonS3.ListObjects
            //TITLE:List S3 objects in bucket
            //DESCRIPTION:List objects in your bucket by prefix.  Keep in mind that buckets with many objects might truncate their results when listing their objects, so be sure to check if the returned object listing is truncated.
            ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix("My"));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println();
            //END_SAMPLE


            System.out.println("Deleting an object\n");

            //BEGIN_SAMPLE:AmazonS3.DeleteObject
            //TITLE:Delete an S3 object
            //DESCRIPTION:Unless versioning has been turned on for your bucket, there is no way to undelete an object, so use caution when deleting objects.
            s3.deleteObject(bucketName, key);
            //END_SAMPLE


            System.out.println("Deleting bucket " + bucketName + "\n");

            //BEGIN_SAMPLE:AmazonS3.DeleteBucket
            //TITLE:Delete an S3 bucket
            //DESCRIPTION:A bucket must be completely empty before it can be deleted, so remember to delete any objects from your buckets before you try to delete them.
            s3.deleteBucket(bucketName);
            //END_SAMPLE
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    /**
     * Creates a temporary file with text data to demonstrate uploading a file
     * to Amazon S3
     *
     * @return A newly created temporary file with text data.
     *
     * @throws IOException
     */
    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890112345678901234\n");
        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890112345678901234\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();

        return file;
    }

    /**
     * Displays the contents of the specified input stream as text.
     *
     * @param input
     *            The input stream to display as text.
     *
     * @throws IOException
     */
    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

}