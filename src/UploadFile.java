import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
public class UploadFile {

	public UploadFile(byte[] soundFileByteArray, String filename) throws Exception {
		
		Storage storage = StorageOptions.getDefaultInstance().getService();
		BlobId blobId = BlobId.of("itx_test1_test2", filename);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		Blob blob = storage.create(blobInfo,soundFileByteArray); 
		String gcsUri = "gs://itx_test1_test2/" + filename;
		System.out.println("Filen er lastet opp under" + gcsUri);
		new ITX(gcsUri);
		
	}
}


