import React, { useState } from "react";

const API_BASE = "http://localhost:8080";

type Order = {
  id: string;
  userId: string;
  status: string;
  amountCents: number;
  description: string;
  createdAt: string;
};

function App() {
  const [userId, setUserId] = useState("user-1");

  const [accountMessage, setAccountMessage] = useState("");
  const [depositAmount, setDepositAmount] = useState(1000);
  const [balance, setBalance] = useState<number | null>(null);

  const [orderAmount, setOrderAmount] = useState(1000);
  const [orderDescription, setOrderDescription] = useState("Test order");
  const [orders, setOrders] = useState<Order[] | null>(null);
  const [orderMessage, setOrderMessage] = useState("");

  async function handleCreateAccount() {
    setAccountMessage("");
    try {
      const resp = await fetch(`${API_BASE}/payments/accounts?userId=${encodeURIComponent(userId)}`, {
        method: "POST",
      });

      if (!resp.ok) {
        const text = await resp.text();
        setAccountMessage(`Ошибка создания счёта: ${resp.status} ${text}`);
        return;
      }

      setAccountMessage("Счёт успешно создан.");
    } catch (e: any) {
      setAccountMessage(`Ошибка сети при создании счёта: ${e.message ?? e}`);
    }
  }

  async function handleDeposit() {
    setAccountMessage("");
    try {
      const resp = await fetch(
        `${API_BASE}/payments/accounts/deposit?userId=${encodeURIComponent(
          userId
        )}&amount=${encodeURIComponent(depositAmount.toString())}`,
        {
          method: "POST",
        }
      );

      if (!resp.ok) {
        const text = await resp.text();
        setAccountMessage(`Ошибка пополнения: ${resp.status} ${text}`);
        return;
      }

      setAccountMessage("Счёт пополнен.");
    } catch (e: any) {
      setAccountMessage(`Ошибка сети при пополнении: ${e.message ?? e}`);
    }
  }

  async function handleLoadBalance() {
    setAccountMessage("");
    try {
      const resp = await fetch(
        `${API_BASE}/payments/accounts/balance?userId=${encodeURIComponent(userId)}`
      );

      if (!resp.ok) {
        const text = await resp.text();
        setAccountMessage(`Ошибка получения баланса: ${resp.status} ${text}`);
        return;
      }

      const data = await resp.json();
      if (typeof data.balance === "number") {
        setBalance(data.balance);
      } else if (typeof data.balance === "string") {
        const parsed = Number(data.balance);
        setBalance(Number.isNaN(parsed) ? null : parsed);
      } else {
        setBalance(null);
      }

      setAccountMessage("Баланс обновлён.");
    } catch (e: any) {
      setAccountMessage(`Ошибка сети при получении баланса: ${e.message ?? e}`);
    }
  }

  async function handleCreateOrder() {
    setOrderMessage("");
    try {
      const body = {
        userId,
        amountCents: Number.isFinite(orderAmount) ? Math.round(orderAmount) : 0,
        description: orderDescription || "Order",
      };

      const resp = await fetch(`${API_BASE}/orders`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      if (!resp.ok) {
        const text = await resp.text();
        setOrderMessage(`Ошибка создания заказа: ${resp.status} ${text}`);
        return;
      }

      const created = (await resp.json()) as Order;
      setOrderMessage(`Заказ создан: ${created.id}`);
    } catch (e: any) {
      setOrderMessage(`Ошибка сети при создании заказа: ${e.message ?? e}`);
    }
  }

  async function handleLoadOrders() {
    setOrderMessage("");
    try {
      const resp = await fetch(`${API_BASE}/orders?userId=${encodeURIComponent(userId)}`);

      if (!resp.ok) {
        const text = await resp.text();
        setOrderMessage(`Ошибка получения заказов: ${resp.status} ${text}`);
        setOrders(null);
        return;
      }

      const data = (await resp.json()) as Order[];
      setOrders(data);
      if (data.length === 0) {
        setOrderMessage("Заказы отсутствуют.");
      } else {
        setOrderMessage(`Загружено заказов: ${data.length}`);
      }
    } catch (e: any) {
      setOrderMessage(`Ошибка сети при получении заказов: ${e.message ?? e}`);
      setOrders(null);
    }
  }

  return (
    <div style={{ fontFamily: "system-ui, -apple-system, BlinkMacSystemFont, sans-serif", padding: "24px", maxWidth: "900px", margin: "0 auto" }}>
      <h1>Gozon Shop Frontend</h1>

      <section style={{ marginBottom: "24px", padding: "16px", border: "1px solid #ccc", borderRadius: "8px" }}>
        <h2>Пользователь</h2>
        <label>
          User ID:&nbsp;
          <input
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            style={{ padding: "4px 8px", minWidth: "200px" }}
          />
        </label>
      </section>

      <section style={{ marginBottom: "24px", padding: "16px", border: "1px solid #ccc", borderRadius: "8px" }}>
        <h2>Платёжный аккаунт</h2>
        <div style={{ display: "flex", gap: "8px", flexWrap: "wrap", marginBottom: "12px" }}>
          <button onClick={handleCreateAccount}>Создать счёт</button>
          <div>
            <label>
              Сумма пополнения:&nbsp;
              <input
                type="number"
                value={depositAmount}
                onChange={(e) => setDepositAmount(Number(e.target.value))}
                style={{ width: "120px" }}
              />
            </label>
            &nbsp;
            <button onClick={handleDeposit}>Пополнить</button>
          </div>
          <button onClick={handleLoadBalance}>Показать баланс</button>
        </div>
        {accountMessage && <p>{accountMessage}</p>}
        {balance !== null && (
          <p>
            Текущий баланс: <strong>{balance}</strong>
          </p>
        )}
      </section>

      <section style={{ marginBottom: "24px", padding: "16px", border: "1px solid #ccc", borderRadius: "8px" }}>
        <h2>Создание заказа</h2>
        <div style={{ display: "flex", flexDirection: "column", gap: "8px", maxWidth: "400px" }}>
          <label>
            Сумма заказа (в центах):&nbsp;
            <input
              type="number"
              value={orderAmount}
              onChange={(e) => setOrderAmount(Number(e.target.value))}
              style={{ width: "200px" }}
            />
          </label>
          <label>
            Описание заказа:&nbsp;
            <input
              type="text"
              value={orderDescription}
              onChange={(e) => setOrderDescription(e.target.value)}
              style={{ width: "100%" }}
            />
          </label>
          <button onClick={handleCreateOrder}>Создать заказ</button>
        </div>
      </section>

      <section style={{ marginBottom: "24px", padding: "16px", border: "1px solid #ccc", borderRadius: "8px" }}>
        <h2>Заказы пользователя</h2>
        <p>Получение списка заказов через Orders Service.</p>
        <button onClick={handleLoadOrders}>Загрузить заказы</button>
        {orderMessage && <p>{orderMessage}</p>}

        {orders === null && <p>Заказы ещё не загружены.</p>}

        {orders && orders.length > 0 && (
          <table style={{ marginTop: "12px", width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr>
                <th style={{ borderBottom: "1px solid #ccc", textAlign: "left", padding: "4px 8px" }}>ID</th>
                <th style={{ borderBottom: "1px solid #ccc", textAlign: "left", padding: "4px 8px" }}>Status</th>
                <th style={{ borderBottom: "1px solid #ccc", textAlign: "left", padding: "4px 8px" }}>Amount (cents)</th>
                <th style={{ borderBottom: "1px solid #ccc", textAlign: "left", padding: "4px 8px" }}>Description</th>
                <th style={{ borderBottom: "1px solid #ccc", textAlign: "left", padding: "4px 8px" }}>Created At</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((o) => (
                <tr key={o.id}>
                  <td style={{ borderBottom: "1px solid #eee", padding: "4px 8px", fontFamily: "monospace" }}>{o.id}</td>
                  <td style={{ borderBottom: "1px solid #eee", padding: "4px 8px" }}>{o.status}</td>
                  <td style={{ borderBottom: "1px solid #eee", padding: "4px 8px" }}>{o.amountCents}</td>
                  <td style={{ borderBottom: "1px solid #eee", padding: "4px 8px" }}>{o.description}</td>
                  <td style={{ borderBottom: "1px solid #eee", padding: "4px 8px" }}>{o.createdAt}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {orders && orders.length === 0 && <p>Заказы отсутствуют.</p>}
      </section>
    </div>
  );
}

export default App;