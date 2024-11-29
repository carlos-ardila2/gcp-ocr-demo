# Contract OCR Demo on Cloud Run

This project demonstrates how to perform Optical Character Recognition (OCR) on contract documents uploaded via an HTTP endpoint using Google Cloud's Document AI and deploy the application to Cloud Run.

## Functionality

The application exposes a POST endpoint `/process_contract` that accepts a file upload (PDF).  It then uses the Document AI API to process the document and extract text entities.  The extracted entities and their corresponding text are returned as a JSON response.

## Project Setup

1. **Prerequisites:**
    *   You'll need a Google Cloud Project with the Document AI API enabled.
    *   Install the Google Cloud SDK ([See Documentation](https://cloud.google.com/sdk/docs/install))
    *   Install Maven
    *   Java 21

2. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/contract-ocr-demo.git  # Replace with your repository URL
    cd contract-ocr-demo
    ```

3. **Set up environment variables:**

   Create a `.env` file in the root directory of the project and add the following variables, replacing the placeholders with your actual values:

    ```bash
    GOOGLE_PROJECT_ID=your-google-project-id
    GOOGLE_OCR_PROCESSOR_ID=your-document-ai-processor-id
    ```

    *   `GOOGLE_PROJECT_ID`:  Your Google Cloud Project ID.
    *   `GOOGLE_OCR_PROCESSOR_ID`: The ID of your Document AI processor.  You'll need to create a processor in the Document AI console trained for the type of contracts you want to process.



## Deployment to Cloud Run

1. **Build the application:**
    ```
    mvn clean install
    ```

2. **Deploy to Cloud Run:**

Use the `gcloud` command-line tool to deploy your application to Cloud Run:

    ```bash
    gcloud run deploy contract-ocr-demo \
      --source . \
      --region us-central1 \
      --allow-unauthenticated \
      --set-env-vars=GOOGLE_PROJECT_ID=${GOOGLE_PROJECT_ID},GOOGLE_OCR_PROCESSOR_ID=${GOOGLE_OCR_PROCESSOR_ID} #Provide ENV variables at deploy time

    ```

    *   Replace `us-central1` with your preferred Cloud Run region. The `--allow-unauthenticated` flag allows public access to the service, and you'll need it to test easily.
    *   You can alternatively use the provided `jib` configuration in the `pom.xml` to build a docker container image and publish that to your projects's Google Container Registry:
        *   `mvn compile jib:build`



## Running Locally



To run locally make sure the `.env` file has the proper GCP Project id and processor id configured, then:

mvn spring-boot:run

## Usage



Once deployed, you can send a POST request to the `/process_contract` endpoint with a file attached as `multipart/form-data`:

curl -X POST -F "file=@path/to/your/ contract. pdf"  https://your-cloud-run- url/ process_ contract



The response will be a JSON object containing the extracted entities and their text.


## Further Development

*   **Error Handling:**  The application includes basic error handling. You can enhance it to provide more specific error messages and handle different types of exceptions more gracefully.
*   **Field Extraction Logic:** The current implementation extracts all entities. You'll likely want to modify the code to extract specific fields based on the schema defined in your Document AI processor.  Refer to the Document AI documentation for details on how to access entity properties.