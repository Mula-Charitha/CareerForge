USE careerforge_db;

-- Fix mulacharithagoud@gmail.com - set password to NewPass@123 (BCrypt hash)
UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy8' 
WHERE email = 'mulacharithagoud@gmail.com' 
  AND password NOT LIKE '$2a$%';

-- Fix 24eg105n08@anurag.edu.in if needed
UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy8' 
WHERE email = '24eg105n08@anurag.edu.in' 
  AND password NOT LIKE '$2a$%';

-- Show current state of all users
SELECT email, full_name, 
  CASE WHEN password LIKE '$2a$%' THEN 'BCrypt OK' ELSE 'PLAIN TEXT - BROKEN' END as password_status
FROM users;
