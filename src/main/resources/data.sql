-- Insert portfolios
insert into portfolios (name, cash_balance) values ('Dividend portfolio', 0);
insert into portfolios (name, cash_balance) values ('My portfolio', 10000.5);
insert into portfolios (name, cash_balance) values ('Growth portfolio', 100);

-- Insert transactions for Dividend portfolio
insert into transactions (id, ticker, type, purchase_date, quantity, price, change_in_cash, commission, portfolio_name)
values
    (nextval('transaction_id_seq'), 'AAPL', 'BUY', '2024-02-19', 100, 150.25, -15025, 10, 'Dividend portfolio'),
    (nextval('transaction_id_seq'), 'AMZN', 'BUY', '2024-02-20', 50, 3300.75, -165037.5, 15, 'Dividend portfolio'),
    (nextval('transaction_id_seq'), 'GOOGL', 'SELL', '2024-02-21', 75, 2900, 217500, 8, 'Dividend portfolio'),
    (nextval('transaction_id_seq'), 'MSFT', 'BUY', '2024-02-22', 200, 330.50, -66100, 20, 'Dividend portfolio'),
    (nextval('transaction_id_seq'), 'TSLA', 'SELL', '2024-02-23', 30, 800.25, 24007.5, 5, 'Dividend portfolio');

-- Insert transactions for My portfolio
insert into transactions (id, ticker, type, purchase_date, quantity, price, change_in_cash, commission, portfolio_name)
values
    (nextval('transaction_id_seq'), 'AAPL', 'BUY', '2024-02-19', 50, 150.25, -7512.5, 12, 'My portfolio'),
    (nextval('transaction_id_seq'), 'AMZN', 'SELL', '2024-02-20', 100, 3300.75, 330075, 15, 'My portfolio'),
    (nextval('transaction_id_seq'), 'GOOGL', 'BUY', '2024-02-21', 75, 2900, -217500, 8, 'My portfolio'),
    (nextval('transaction_id_seq'), 'MSFT', 'BUY', '2024-02-22', 120, 330.50, -39660, 10, 'My portfolio'),
    (nextval('transaction_id_seq'), 'TSLA', 'SELL', '2024-02-23', 80, 800.25, 64020, 12, 'My portfolio'),
    (nextval('transaction_id_seq'), 'AAPL', 'BUY', '2024-02-24', 90, 150.25, -13522.5, 9, 'My portfolio'),
    (nextval('transaction_id_seq'), 'AMZN', 'SELL', '2024-02-25', 60, 3300.75, 198045, 20, 'My portfolio'),
    (nextval('transaction_id_seq'), 'GOOGL', 'BUY', '2024-02-26', 110, 2900, -319000, 14, 'My portfolio'),
    (nextval('transaction_id_seq'), 'MSFT', 'SELL', '2024-02-27', 70, 330.50, 23135, 10, 'My portfolio'),
    (nextval('transaction_id_seq'), 'TSLA', 'BUY', '2024-02-28', 100, 800.25, -80025, 8, 'My portfolio'),
    (nextval('transaction_id_seq'), 'AAPL', 'BUY', '2024-02-29', 85, 150.25, -12771.25, 11, 'My portfolio'),
    (nextval('transaction_id_seq'), 'AMZN', 'SELL', '2024-03-01', 95, 3300.75, 313527.25, 18, 'My portfolio'),
    (nextval('transaction_id_seq'), 'GOOGL', 'BUY', '2024-03-02', 130, 2900, -377000, 15, 'My portfolio'),
    (nextval('transaction_id_seq'), 'MSFT', 'BUY', '2024-03-03', 110, 330.50, -36355, 12, 'My portfolio'),
    (nextval('transaction_id_seq'), 'TSLA', 'SELL', '2024-03-04', 75, 800.25, 60018.75, 10, 'My portfolio');
