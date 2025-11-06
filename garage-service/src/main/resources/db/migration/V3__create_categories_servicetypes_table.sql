CREATE TABLE categories
(
    id   BINARY(16)   NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE service_types
(
    id               BINARY(16)    NOT NULL,
    name             VARCHAR(255)  NOT NULL,
    `description`    LONGTEXT      NOT NULL,
    price            DECIMAL(10,2) DEFAULT 0.00,
    duration_minutes INT           DEFAULT 0,
    category_id      BINARY(16)    NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE service_types
    ADD CONSTRAINT fk_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
            ON DELETE NO ACTION;

CREATE INDEX fk_category ON service_types (category_id);

-- --------------------------------------------------------
-- Data for table `categories` (The Major Vehicle Systems)
-- Using UUID_TO_BIN(UUID()) to generate genuine BINARY(16) IDs upon insertion.
-- --------------------------------------------------------

-- Define a set of UUIDs to ensure proper foreign key linking in the service_types table.
-- In a real migration, you would capture these generated IDs or use a transactional approach.
-- For this file, we must generate them first and store them in variables (MySQL syntax).
SET @EngineId = UUID_TO_BIN(UUID());
SET @BrakesId = UUID_TO_BIN(UUID());
SET @TiresId = UUID_TO_BIN(UUID());
SET @SuspensionId = UUID_TO_BIN(UUID());
SET @ElectricalId = UUID_TO_BIN(UUID());
SET @HVACId = UUID_TO_BIN(UUID());
SET @DrivetrainId = UUID_TO_BIN(UUID());
SET @ExteriorId = UUID_TO_BIN(UUID());

INSERT INTO categories (id, name) VALUES
                                      (@EngineId, 'Engine & Performance'),
                                      (@BrakesId, 'Brakes'),
                                      (@TiresId, 'Tires & Wheels'),
                                      (@SuspensionId, 'Steering & Suspension'),
                                      (@ElectricalId, 'Electrical & Battery'),
                                      (@HVACId, 'Heating & Cooling (HVAC)'),
                                      (@DrivetrainId, 'Drivetrain & Transmission'),
                                      (@ExteriorId, 'Exterior, Visibility & Safety');

-- --------------------------------------------------------
-- Data for table `service_types`
-- Now using UUID_TO_BIN(UUID()) for the service IDs and linking via the category variables.
-- --------------------------------------------------------

-- I. Engine & Performance (@EngineId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @EngineId, 'Air and Fuel Filter Replacement', 'Replacement of the engine air filter and fuel filter to maintain optimal combustion and engine health.', 45.00, 30),
        (UUID_TO_BIN(UUID()), @EngineId, 'Engine Diagnostics & Tune-Up', 'Full computer scan for Check Engine Light codes, spark plug replacement, and ignition system check.', 120.00, 90),
        (UUID_TO_BIN(UUID()), @EngineId, 'Timing Belt Replacement', 'Replacement of the timing belt and associated tensioners/pulleys (critical preventative maintenance).', 450.00, 240);


-- II. Brakes (@BrakesId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @BrakesId, 'Brake System Inspection', 'Comprehensive inspection of all brake components, including pads, rotors, and fluid condition.', 35.00, 30),
        (UUID_TO_BIN(UUID()), @BrakesId, 'Brake Pad and Rotor Replacement (Axle)', 'Replacement of front or rear brake pads and resurfacing or replacement of rotors on one axle.', 180.00, 90),
        (UUID_TO_BIN(UUID()), @BrakesId, 'Brake Fluid Flush', 'Complete exchange of old brake fluid to maintain hydraulic system integrity and prevent corrosion.', 90.00, 60);

-- III. Tires & Wheels (@TiresId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @TiresId, 'Tire Rotation and Pressure Check', 'Rotating tires to promote even wear and checking/adjusting tire pressures.', 25.00, 30),
        (UUID_TO_BIN(UUID()), @TiresId, 'Four-Wheel Alignment', 'Adjusting the angles of the wheels to meet manufacturer specifications for improved handling and tire life.', 110.00, 60),
        (UUID_TO_BIN(UUID()), @TiresId, 'Tire Repair (Patch)', 'Professional repair of a puncture using a plug and patch method.', 30.00, 45);

-- IV. Steering & Suspension (@SuspensionId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @SuspensionId, 'Suspension Inspection and Check', 'Comprehensive check of shocks, struts, and steering components for wear or damage.', 50.00, 45),
        (UUID_TO_BIN(UUID()), @SuspensionId, 'Shock/Strut Replacement (Pair)', 'Replacement of both front or rear shock absorbers or strut assemblies.', 350.00, 180);

-- V. Electrical & Battery (@ElectricalId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @ElectricalId, 'Battery Test and Replacement', 'Testing battery health, cleaning terminals, and professional installation of a new battery if needed.', 20.00, 30),
        (UUID_TO_BIN(UUID()), @ElectricalId, 'Alternator Replacement', 'Removal and replacement of the alternator, ensuring the vehicle’s charging system is fully functional.', 280.00, 120);

-- VI. Heating & Cooling (HVAC) (@HVACId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @HVACId, 'Cooling System Flush', 'Flush old engine coolant and replace with fresh fluid to prevent overheating and corrosion.', 110.00, 60),
        (UUID_TO_BIN(UUID()), @HVACId, 'A/C Performance Check & Recharge', 'Diagnostic test of the air conditioning system and refrigerant recharge to restore cooling performance.', 140.00, 90);

-- VII. Drivetrain & Transmission (@DrivetrainId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @DrivetrainId, 'Transmission Fluid Service', 'Drain old transmission fluid and replenish with new fluid (non-flush service).', 130.00, 60),
        (UUID_TO_BIN(UUID()), @DrivetrainId, 'Clutch Replacement (Manual)', 'Replacement of the clutch disc, pressure plate, and throw-out bearing.', 650.00, 360);

-- VIII. Exterior, Visibility & Safety (@ExteriorId)
INSERT INTO service_types (id, category_id, name, description, price, duration_minutes) VALUES
        (UUID_TO_BIN(UUID()), @ExteriorId, 'Wiper Blade Replacement', 'Replacement of front and/or rear windshield wiper blades.', 20.00, 15),
        (UUID_TO_BIN(UUID()), @ExteriorId, 'Headlight Bulb Replacement', 'Replacement of one or both headlight bulbs.', 35.00, 30);