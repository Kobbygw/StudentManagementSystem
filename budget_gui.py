import tkinter as tk
from tkinter import messagebox

class BudgetApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Monthly Budget Calculator")

        # Variables
        self.variable_expenses = []

        # Income Input
        tk.Label(root, text="Monthly Income:").grid(row=0, column=0, sticky="w")
        self.income_entry = tk.Entry(root)
        self.income_entry.grid(row=0, column=1)

        # Fixed Expenses Input
        tk.Label(root, text="Fixed Expenses:").grid(row=1, column=0, sticky="w")
        self.fixed_entry = tk.Entry(root)
        self.fixed_entry.grid(row=1, column=1)

        # Variable Expense Inputs
        tk.Label(root, text="Variable Expense Name:").grid(row=2, column=0, sticky="w")
        self.var_name_entry = tk.Entry(root)
        self.var_name_entry.grid(row=2, column=1)

        tk.Label(root, text="Amount:").grid(row=3, column=0, sticky="w")
        self.var_amount_entry = tk.Entry(root)
        self.var_amount_entry.grid(row=3, column=1)

        # Buttons
        tk.Button(root, text="Add Variable Expense", command=self.add_variable_expense).grid(row=4, column=0, columnspan=2, pady=5)
        tk.Button(root, text="Calculate Budget", command=self.calculate_budget).grid(row=5, column=0, columnspan=2, pady=5)

        # Output Label
        self.output_label = tk.Label(root, text="", fg="blue")
        self.output_label.grid(row=6, column=0, columnspan=2)

    def add_variable_expense(self):
        name = self.var_name_entry.get()
        try:
            amount = float(self.var_amount_entry.get())
        except ValueError:
            messagebox.showerror("Input Error", "Amount must be a number.")
            return

        self.variable_expenses.append((name, amount))
        self.var_name_entry.delete(0, tk.END)
        self.var_amount_entry.delete(0, tk.END)
        messagebox.showinfo("Added", f"{name} - ${amount:.2f} added.")

    def calculate_budget(self):
        try:
            income = float(self.income_entry.get())
            fixed = float(self.fixed_entry.get())
        except ValueError:
            messagebox.showerror("Input Error", "Income and fixed expenses must be numbers.")
            return

        variable_total = sum(amount for _, amount in self.variable_expenses)
        total_expenses = fixed + variable_total
        remaining = income - total_expenses

        if remaining > 0:
            message = f"You have ${remaining:.2f} left after expenses."
        elif remaining == 0:
            message = "You have used your entire income."
        else:
            message = f"You are over budget by ${-remaining:.2f}."

        self.output_label.config(text=message)


# Run the app
root = tk.Tk()
app = BudgetApp(root)
root.mainloop()
