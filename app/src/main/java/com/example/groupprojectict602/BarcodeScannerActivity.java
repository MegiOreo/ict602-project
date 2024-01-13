//package com.example.groupprojectict602;
//
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageProxy;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//
//// ... other imports
//
//public class BarcodeScannerActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_barcode_scanner);
//
//        PreviewView previewView = findViewById(R.id.previewView);
//
//        // Initialize camera provider
//        ProcessCameraProvider cameraProvider = ...; // Initialize this as per CameraX documentation
//
//        cameraProvider.bindToLifecycle(this, cameraSelector, previewView, imageAnalysis);
//
//        // Implement ImageAnalysis to get barcode
//        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .build();
//
//        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
//            @Override
//            public void analyze(@NonNull ImageProxy image) {
//                // Use ML Kit or other methods to detect and decode the barcode
//                // After decoding, return the barcode value to the SearchFragment
//                String barcodeValue = ...; // Decoded barcode value
//
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("BARCODE_RESULT", barcodeValue);
//                setResult(Activity.RESULT_OK, resultIntent);
//                finish();
//            }
//        });
//    }
//}
//
