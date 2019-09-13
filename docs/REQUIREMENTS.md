# Requirements

* [Mobile Application](#mobile-application)
* [Web Application](#web-application)
  * [Access Levels](#access-levels)
    * [VHT](#vht)
    * [Health Workers](#health-workers)
    * [Statistical Analyser](#statistical-analyser)
    * [Administrator](#administrator)
* [Security](#security)
* [Backend](#backend)
  * [API](#api)
  * [Database](#database)

## Mobile Application



## Web Application

### Access Levels

> Question: Do patients get access to this and if so what can they view?

* The web application must support 4 levels of access
  * Must have a way for users to log in
  * The system must be able to determine the level of access for the current user when they make a request to the server

    > Impl. Note: One possible way to do this would be to include an authentication token in all API requests to the server that would be used identify who the request came from.

#### VHT

* VHTs must have access to **only** the records for the patients that they work with
  * This implies that we need to have a way to uniquely identify patients

    > Impl. Note: There was mention of using phone numbers to uniquely identify patients which may work, but there are a few issues.
    >
    > * What happens if someone changes their phone number?
    > * What about patients that do not own a phone
    >
    > We may want to look into other ways to uniquely identifying patients.

* VHTs should be able to create new readings and referrals for a patient
  * This includes not only the result of the CRADLE reading but also symptoms

* VHTs should be able to follow-up with (view changes on a patient's profile) a patient after their visit to a health center
  * Changes that the health worker makes on the patient's profile should be visible by the VHT
    * For example, if the health worker requests an additional reading in a few days, the VHT should be notified about it.

#### Health Workers

* Health workers need access to **only** the patients which are referred to them
  * Health workers in center *A* only need access to patients admitted to center *A* and not those admitted to center *B*
    * Need to keep track of a list of centers
      * Need the ability to add or remove centers in the event of a new one being created or one being decommissioned (probably only want to give Administrators this access)
    * Need to keep track of which health workers work at which center

* Health workers need the ability to edit a patient's profile
  * Add readings, symptoms, recommend procedures, etc.

    > Question: Need to fully clarify what the health workers should be able to do.

* Health workers should be able to view the entire history for a patient
  * This includes health, and medication trends
  * Should be viewable as graphs, lists and, possibly, as raw data

* Health workers should be able to access referral information in **real time**

  > Impl. Note: This may warrant some sort of notification system for the web application.
  >
  > One idea would be to have some sort of "dashboard" or "home page" which would be the first thing a user sees when they log in and displays relevant information: including notifications about new referrals.

#### Statistical Analyser

* Analysers should have access to most data that has been collected
  * This data should be sanitized: personal identification information (people's names, etc.) should be omitted

    > Question: Need to fully clarify what statistics should be logged

  * Analysers should be able to view the data as a graph or in its raw form (CSV/Excel file for example)

* Analysers may not edit any of the data (readonly access)

#### Administrator

* Administrators should have read/write access to everything

* Only a select few people should be Administrators
  * Developers
  * Research leads


## Security

Since we are dealing with health information we need to be very careful about how we handle it. As of writing this, we don't know if there are any special laws or guidelines that we need to conform to, so the following are simply suggestions.

* Transmissions between the application should be encrypted

  > Impl. Note: There are two ways we could go about this, either rely on HTTPS, or manually encrypt/decrypt data before and after transmission on both ends.
  >
  > I'd recommend the latter: using some standard encryption algorithm (e.g., AES-128 or RSA to encrypt the body of an HTTP request before sending it to the server). The reason for this is that we won't need to worry about creating or acquiring the required certificates needed for HTTPS. Though this can be subject for debate if others have better ideas.

* Some data should be stored encrypted in the database
  * What data should and should not be encrypted is subject to further analysis

    > Impl. Question: How to go about storing encryption keys in a safe and secure way?

* Need to enforce strict user permissions according the defined [access levels](#access-levels)

## Backend

* Both the mobile and web applications must be able to sync with a common server

* Server should expose a public API which the mobile and web applications will use to update and pull data from the database (see [API](#api))

### API

* The API must be secure (see [Security](#security))
  * i.e., only authorized personal should be able to `GET` / `POST` data

* Must have a way to create new patients

* Must have a way to upload a reading for a patient
  * Only VHTs and Health Workers should have access to this method

* Must have a way to create a referral for a specific patient

* Must have a way to query/update information about a patient
  * This information should include:
    * Personal information: name, age, pregnant or not, etc.
    * History of blood pressure readings
    * History of medication
    * History of symptoms
    * Other important information
  * This information should be available to:
    * VHTs (read/write)
    * Health Workers (read/write)
    * Statistical Analysers (readonly by proxy)
      * The analysers should be able to view histories and trends as a whole; they should not be able to relate an individual history/reading back to a specific patient

* Must have a way to query the patients for a particular VHT
  * Needed so that a VHT can view their patients

* Must have a way to query various information about a specific VHT
  * e.g., number of readings performed, number of referrals made, etc.
  * Needed for VHT monitoring

* Must have a way to query the patients for a particular Health Worker
  * Needed so that a Health Worker can view their patients

* Must have a way to query for notification for a specific user

### Database

* The database must be secure so that in the possible event of a data breach, personal health information is safe (see [Security](#security))

* The database should have some sort of replication system to prevent against data loss in the event of a failed HDD or server
  * Note: This is probably outside of the scope of the course project, but would be and absolute necessity for production use. We should avoid making any design decisions which would make it difficult to implement.
