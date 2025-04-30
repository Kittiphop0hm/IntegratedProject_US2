use INT221_db;
INSERT INTO saleItem (id, brand_id, model, description, quantity, price, screenSizeInch, ramGb, storageGb, color) VALUES
(1, 2, 'iPhone 14 Pro Max', 'ไอโฟนเรือธงรุ่นล่าสุด มาพร้อม Dynamic Island จอใหญ่สุดในตระกูล กล้องระดับโปร', 5, 42900, 6.7, 6, 512, 'Space Black'),
(2, 2, 'iPhone 14', 'ไอโฟนรุ่นใหม่ล่าสุด รองรับ5G เร็วแรง ถ่ายภาพสวยทุกสภาพแสง', 8, 29700, 6.1, 6, 256, 'Midnight'),
(3, 2, 'iPhone 13 Pro', 'ไอโฟนรุ่นโปร จอ ProMotion 120Hz กล้องระดับมืออาชีพ', 3, 33000, 6.1, 6, 256, 'Sierra Blue'),
(7, 2, 'iPhone SE 2022', 'Budget-friendly model', 15, 14190, 4.7, 4, 64, 'Starlight'),
(8, 2, 'iPhone 14 Plus', 'iPhone 14 Plus 128GB สี Starlight เครื่องศูนย์ไทย โมเดล TH แท้ 100% มีกล่องครบ ประกันศูนย์ พ.ย. 68 ส่งฟรี', 7, 29700, 6.7, 6, 256, 'Blue'),
(16, 1, 'Galaxy S23 Ultra', 'Samsung Galaxy S23 Ultra 512GB สีดำมิดไนท์ กล้องเทพพังพาบ 99% ไร้รอย แถมเคสแท้ แบตอึดสุดๆ รองรับปากกา S-Pen', 1, 32900, 7.6, NULL, 512, NULL);
select * from saleItem;
-- SET SQL_SAFE_UPDATES = 0;
-- delete from saleItem;
