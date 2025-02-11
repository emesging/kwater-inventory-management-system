k water Inventory Management System

개요

이 프로젝트는 Hibernate와 Java Swing을 사용하여 구축된 재고 관리 시스템입니다. 사용자는 제품을 추가, 검색, 수정 및 삭제할 수 있으며, Hibernate ORM을 활용하여 MySQL 데이터베이스에 데이터를 저장합니다.

기능

제품 관리: 제품을 추가, 수정, 삭제할 수 있습니다.

재고 관리: 특정 제품의 입고 및 출고를 관리할 수 있습니다.

주문 관리: 고객이 주문을 생성하고 제품과 연결할 수 있습니다.

고객 관리: 고객 정보를 저장하고 검색할 수 있습니다.

로그인 관리: 사용자 로그인 정보를 관리합니다.

주요 클래스

1. HibernateCFGcode.java

Hibernate 설정을 담당하는 클래스입니다.

sessionfactory를 생성하고, 세션을 열어 데이터베이스와 상호작용할 수 있도록 합니다.

2. InventoryApp.java

Java Swing을 활용하여 GUI를 제공하는 메인 애플리케이션입니다.

제품 추가, 검색, 수정, 삭제 기능을 포함합니다.

테이블 정렬 및 검색 기능을 제공합니다.

3. InventoryHistory.java

제품 입출고 내역을 저장하는 엔티티 클래스입니다.

product_id, quantity, action, date 등의 정보를 저장합니다.

4. Product.java

제품 정보를 저장하는 엔티티 클래스입니다.

pid, pname, price, pquantity, plocationField, pdetailLocationField 속성을 포함합니다.

제품 정보가 변경될 때 updated_at 필드가 자동 업데이트됩니다.

5. Customer.java

고객 정보를 저장하는 엔티티 클래스입니다.

cid, cname, ccontact, cemail 속성을 포함합니다.

6. Order.java

주문 정보를 저장하는 엔티티 클래스입니다.

고객과 제품을 연결하고, quantity, orderDate 정보를 저장합니다.

7. Login.java

사용자 로그인 정보를 저장하는 엔티티 클래스입니다.

username, password 속성을 포함합니다.

데이터베이스 설정

Hibernate.cfg.xml 파일을 설정하여 MySQL 데이터베이스에 연결합니다.

데이터베이스 스키마는 Hibernate가 자동으로 생성할 수 있도록 설정되어 있습니다.

실행 방법

MySQL 데이터베이스를 실행하고 Hibernate.cfg.xml 설정을 확인합니다.

InventoryApp.java를 실행하여 GUI를 실행합니다.

제품을 추가, 수정, 삭제하고 검색 기능을 테스트합니다.

개발 환경

언어: Java

GUI 프레임워크: Java Swing

ORM: Hibernate

데이터베이스: MySQL

빌드 도구: Maven 또는 직접 JAR 실행

향후 개선 사항

사용자 인증 기능 강화 (암호화 적용)

REST API 추가하여 웹 프론트엔드와 연동

제품 재고 변화에 대한 알림 기능 추가

