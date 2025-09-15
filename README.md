# Blood Donation Organizer (Java CLI)

A simple Java command-line application to manage blood donors, search by blood group and location, validate eligibility based on donation intervals, and provide reminders. Data is stored persistently in a CSV file.

## Features
- Register new donors with validation (age >= 18, valid blood groups, proper dates)
- Search donors by blood group and location
- Eligibility checks using a minimum 3-month interval
- Donation reminder shows next eligible date
- Persistent storage in `data/donors.csv`

## Requirements
- Java 17+ (or any version that supports `java.time` and `javac`)

## Project Structure
```
blood-donation-cli/
  ├─ src/main/java/com/example/blood/
  │   ├─ App.java
  │   ├─ ConsoleUI.java
  │   ├─ Donor.java
  │   ├─ DonorRepository.java
  │   ├─ EligibilityService.java
  │   └─ Validator.java
  └─ data/donors.csv (auto-created on first run)
```

## Build and Run
From the repository root:

```bash
# Compile
javac -d blood-donation-cli/out $(git ls-files 'blood-donation-cli/src/main/java/**/*.java' 2>NUL || dir /s /b blood-donation-cli\src\main\java\*.java)

# Run
java -cp blood-donation-cli/out com.example.blood.App
```

On Windows PowerShell without Git, you can compile like this:

```powershell
$src = Get-ChildItem -Recurse blood-donation-cli/src/main/java/*.java | ForEach-Object { $_.FullName }
javac -d blood-donation-cli/out $src
java -cp blood-donation-cli/out com.example.blood.App
```

## Usage
Follow the on-screen menu to:
- Register a new donor
- Search donors
- Check eligibility
- Exit

Dates should be entered as `YYYY-MM-DD`. If the donor has never donated, leave the date blank during registration.

## Notes
- Valid blood groups: A+, A-, B+, B-, AB+, AB-, O+, O-
- Age validation: 18–65 years
- Data file is appended on new registrations to keep I/O simple
- You can safely move or delete `blood-donation-cli/data/donors.csv` if you want to reset

## Possible Improvements
- Partial and nearby-location search (e.g., fuzzy match or distance)
- Update and delete donor records
- Better contact validation and optional email/phone separation
- Switch to JSON with a schema and add unit tests 