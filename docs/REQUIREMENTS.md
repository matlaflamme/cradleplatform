# Requirements

## Web Application

### Access Levels

> Question: Do patients get access to this and if so what can they view?

* The web application must support 3 levels of access
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
