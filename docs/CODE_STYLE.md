# Coding Convention

This file lays out the coding convention for this project. These conventions take inspiration from [this](https://opencoursehub.cs.sfu.ca/bfraser/grav-cms/cmpt373/2019-7/links/files/CodeStyleGuide.html) style guide created by Brian Fraser. 

## Contents

- [Commit Messages](#commit-messages)
- [Java](#java)
  - [Naming](#naming)
  - [Comments](#comments)
  - [Formatting](#formatting)
  - [Package Structure](#package-structure)
    - [The Controller Package](#the-controller-package)
    - [The Model Package](#the-model-package)
    - [The Service Package](#the-service-package)
  - [Annotations](#annotations)
- [Android App](#android-app)
- [JavaScript](#javascript)
- [HTML](#html)

## Commit Messages

When committing your work, it can be tedious when looking through all of our files. It is imperative that we keep a standard convention/style for our messages. Below are some of the guidelines that you are expected to follow beginning **09/29/19**:

| **Message** | **Description**                                    | **Example**                        |
| ----------- | -------------------------------------------------- | ---------------------------------- |
| `[FIX]`     | You have fixed errors/bugs within a file (or more) | `[FIX]: Directory Path Correction` |
| `[ADD]`     | You added a new file (or more) or feature(s)       | `[ADD]: Added loginPage.html`      |
| `[DEL]`     | You have deleted a file (or more) or feature(s)    | `[DEL]: Removed errorFile.java`    |

If you are working as a pair (or more), you can add your names in square brackets (lower-case) and separated by commas prefixing the above requirement:

| **Example**                                                  |
| ------------------------------------------------------------ |
| `[parmis,mathieu][ADD]: allPatientView.html, js, css`        |
| **Description**                                              |
| Parmis and Mathieu both worked to add the new files: `allPatientView.html`, `.js`, and `.css` |



## Commit Messages

When committing your work, it can be tedious when looking through all of our files. It is imperative that we keep a standard convention/style for our messages. Below are some of the guidelines that you are expected to follow beginning **09/29/19**:

| **Message** | **Description**                                    | **Example**                        |
| ----------- | -------------------------------------------------- | ---------------------------------- |
| `[FIX]`     | You have fixed errors/bugs within a file (or more) | `[FIX]: Directory Path Correction` |
| `[ADD]`     | You added a new file (or more) or feature(s)       | `[ADD]: Added loginPage.html`      |
| `[DEL]`     | You have deleted a file (or more) or feature(s)    | `[DEL]: Removed errorFile.java`    |

If you are working as a pair (or more), you can add your names in square brackets (lower-case) and separated by commas prefixing the above requirement:

| **Example**                                                  |
| ------------------------------------------------------------ |
| `[parmis,mathieu][ADD]: allPatientView.html, js, css`        |
| **Description**                                              |
| Parmis and Mathieu both worked to add the new files: `allPatientView.html`, `.js`, and `.css` |



## Java

### Naming

Classes, interfaces, enumerations, and other top-level declarations should use UpperCamelCase.

```java
interface DatabaseContext { ... }
```

Methods and properties should use lowerCamelCase.

```java
void doStuff(int someParameter) { ... }
```

Constants should use all uppercase letters with underscores.

```java
long MY_NUMBER = 5;
```

When appropriate, interfaces should try and use an "-able" suffix. If not appropriate for a specific context, a simple noun describing the interface is also acceptable.

```java
// Not a great interface name
interface ToInt {
    int toInt();
}

// A better interface name
interface IntegerConvertible {
    int toInt();
}

// A case where an "-able" suffix doesn't really work
interface DatabaseContext {
    ...
}
```

### Comments

Each public or package-private class, interface, enumeration, etc. should have a class level JavaDoc comment describing the intent of the class, interface, etc.

**JavaDoc Conventions**:

- **Don't** use the `@author` tag, that's what Git is for
- Use the `@see` tag to link to related classes/methods
  - This tag works really nicely in Intellij so use it whenever acceptable
- When documenting a method...
  - There must be a `@param` tag for each parameter
  - If the method returns someting, there must be a `@return` tag
  - If the method throws an exception, there must be a `@throws` tag explaining the conditions for the exception to be thrown
- **Don't** comment simple setters and getters
- **Do** comment any method that is reasonably complex or has side effects

### Formatting

Use tabs instead of spaces when indenting.

> **Important**: Please configure your editor to automatically insert tabs when indenting instead of spaces.
>
> In Intellij this can be found under: 'Preferences > Editor > Code Style > Java' and, on the "Tabs and Indents" tab, check the box "Use tab character".

```java
if (condition) {
⟼doStuff();
}
```

Don't add a space between an opening/closing parentheses and whatever is inside. The same goes for subscripts.

```java
// do
while (true) { ... }
array[computeIndex()];

// don't
while ( true ) { ... }
array[ computeIndex() ];
```

Use 1 space between a control structure keyword and its condition, and 1 space after the condition and before the opening brace.

```java
while∙(true)∙{
⟼doStuff();
}
```

Use 1 space between binary operators and their operands, and no spaces between unary operators and their operand.

```java
boolean x∙=a∙||∙(b∙&&∙!c);
```

### Package Structure

All Java code must be written under the root package `com.cradlerest.web` located under `/web/src/main/java`.

Package names should be a singular noun, all lowercase (e.g., `model` instead of `models`).

Three primary packages are located under the root package: `controller`, `model`, and `service`.

Along with the 3 primary packages, additional support packages may be created if needed (e.g., `util`, `parse`, etc.)

#### The Controller Package

Package `com.cradlerest.web.controller` should contain various REST and MVC controllers: implementing the logic for handling various requests.

Controller class names should end in `Controller`.

Controllers should be specific, handling a single domain, rather than general purpose.

```java
// Controller handling only patient related requests - GOOD
@RestController
class PatientController { ... }

// The whole API - BAD
@RestController
class ApiController { ... }

```

#### The Model Package

Package `com.cradlerest.web.model` should contain various classes for the data models being passed around by the database and server (e.g., `Patient`, `VHT`, etc.).

Model class names should be nouns.

Model classes should be plain-old-java-objects with their primary purpose being to store data (i.e., simple with no complex logic, like structs in languages like C, Go, or Rust).

#### The Service Package

Package `com.cradlerest.web.service` should contain various contexts/services which may be utilized by controllers via dependency injection.

Services should have a corresponding interface that they implement so that they can be easily switched out with different implementations if the need arises. This also allows us to create *mock* services which have a temporary implementation which can be used for testing or as a placeholder until the actual implantation is built.

A sub-package of `service`, `com.cradlerest.web.service.config` will contain the various `Bean` factories required by Spring's dependency injection. It is possible to do this via an XML config file, but lets use Java instead.

Bean factory classes should be named after the service they produce and end in `Config` (e.g., `DatabaseContextConfig` for generating `DatabaseContext` instances).

An example of a simple service for generating id numbers:

```java
// file: com/cradlerest/web/service/IdGenerator.java

// The service interface.
public interface IdGenerator {
    long getId();
}


// file: com/cradlerest/web/service/IncrementingIdGenerator.java

// An implementation of the service interface.
public class IncrementingIdGenerator implements IdGenerator {
    private final AtomicLong counter = new AtomicLong();

    @Override
    long getId() {
        return counter.getAndIncrement();
    }
}


// file: com/cradlerest/web/service/config/IdGeneratorConfig.java

// The service factory used in dependency injection.
@Configuration
public class IdGeneratorConfig {
    @Bean
    @Scope("singleton") // not absolutely necessary as "singleton" is the default
    IdGenerator incrementingIdGenerator() {
        return new IncrementingIdGenerator();
    }
}

```

### Annotations

Always use the `@Override` annotation when overriding a method from a super class or interface.

```java
@Override
public boolean equals(Object obj) { ... }

```

Intellij has `@Nullable` and `@NotNull` annotations which allows the IDE to highlight places where a `NullPointerException` may occur. For ease of development and to catch more errors at/before compile-time please make use of these annotations wherever applicable.

```java
// A method without annotations.
void printStringLength(String str) {
    var length = str.length(); // NullPointerException if str == null
    System.out.printf("length = %d\n", length);
}

// A method with @NotNull annotation.
// The IDE will warn you if you try and pass null or a @Nullable string to this
// method without first checking if it is not null.
void printStringLength(@NotNull String str) {
    var length = str.length();
    System.out.printf("length = %d\n", length);
}

// A method with @Nullable annotation.
// The @Nullable annotation tells the IDE and other programmers that it is ok 
// to pass null to this method as it will explicitly handle that case.
void printStringLength(@Nullable String str) {
    if (str == null) {
        System.out.println("string is null");
    } else {
        var length = str.length();
        System.out.printf("length = %d\n", length);
    }
}

```

`@Nullable` and `@NotNull` work on properties and return types as well.

```java
class Person {
    @NotNull
    private final String firstName;

    @Nullable
    private final String middleName;

    @NotNull
    private final String lastName;

    // --snip--

    @Nullable
    public String getMiddleName() {
        return middleName;
    }

    @NotNull
    public String getLastName() {
        return lastName;
    }
}

```

## Android App

When working on the modifying the Android application, try and match the style of the surrounding code. For example, if 4 spaces are used for indentation, use 4 spaces instead of a tab.

If it is unclear what style to use, the above [Java](#java) style guide may be used.

> **Important**: **Never mix tabs and spaces** for indentation! When working on the Android app, always use the same indentation style as the surrounding code.



## JavaScript

When working with `.js` files, we primarily use them for **Vue Applications**. Vue variables are to be under `let`, not `var`. 

All Javascript files are to be put into the `js` sub-folder of our *webapp* main folder. 

Names should be in *camelCase* style -- **no underscores**. Vue variable should have the same name as the element `id` to reduce confusion and possible issues down the road. 

Curly braces (`{}`) should be separated from any variables/data sets with **one space**. Additionally, colons (`:`) and parentheses (`()`) are to stick to the variable/data set.  The opening bracket will always be on the same line as what you are declaring, while the closing bracket will be on its own line with matching indentation as its declared counterpart.

Equal signs (`=`) are to be separated by **one space** (note: `==` and `===` can stay conjoined -- obviously).

No real requirements for Semi-colons. That is up to you.

For example:

```javascript
let loginInput = new Vue({
    el: '#loginInput',
    data: {
        user: '',
        pass: ''
    }
})

```



## HTML

All `<script src=... /> ` tags are to be presented at the top of the HTML file within the `<head />` tags. 

Indentations must be used with a singular use of the `TAB`  key -- **do not press TAB twice per indentation**. Each primary element (i.e., containers) will have their own indentation column with their nested elements appropriately indented once recursively. 

Unless absolutely necessary (e.g., screen overflow), each element will be contained in a single line. 

The element's `id` will be the first on the list of properties, proceeding appropriate element `type`. 

Due to the nature of elements being long declarations, comments are to be placed **above** their respective line(s) of code. If necessary, separate each "chunk" of code by a blank space. 

Line breaks can be placed either at the end of (short) lines as well as between two separated "chunks." Continuous streams of breaks can be conjoined into one line.

For example:

```html
<!--This is my comment for myDiv-->
<div id="registration">
    <input type="text" id="user" placeholder="Username">
    <br><br>
    <!--Submit button-->
    <button id="submit">Submit</button>
</div>

```

