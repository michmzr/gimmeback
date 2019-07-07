package com.michmzr.gimmeback.loan;

import org.springframework.data.jpa.repository.JpaRepository;

interface LoanRepository extends JpaRepository<Loan, Long> {
}
