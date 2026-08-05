import re
import pandas as pd
import matplotlib.pyplot as plt

# Paste your Java output between the triple quotes below
java_output = """
Added data point: price=506.049988, timestamp=2026-05-26T10:28:36.547871559Z
Added data point: price=507.120026, timestamp=2026-05-26T10:29:36.547871559Z
Added data point: price=505.899994, timestamp=2026-05-26T10:30:36.547871559Z
Added data point: price=508.450012, timestamp=2026-05-26T10:31:36.547871559Z
Added data point: price=509.200012, timestamp=2026-05-26T10:32:36.547871559Z
"""

# Extract price and timestamp
matches = re.findall(
    r"price=([0-9.]+), timestamp=([^\n]+)",
    java_output
)

# Convert to DataFrame
df = pd.DataFrame(matches, columns=["price", "timestamp"])

# Convert data types
df["price"] = df["price"].astype(float)
df["timestamp"] = pd.to_datetime(df["timestamp"])

# Display the extracted data
print(df)

# Plot the data
plt.figure(figsize=(10, 5))
plt.plot(df["timestamp"], df["price"], marker="o")

plt.title("DIA Price Over Time")
plt.xlabel("Timestamp")
plt.ylabel("Price")

plt.xticks(rotation=45)
plt.grid(True)
plt.tight_layout()
plt.show()