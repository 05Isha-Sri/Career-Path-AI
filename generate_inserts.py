import sys
import os
import openpyxl
import shutil

def clean_text(val):
    if val is None:
        return ""
    # Convert value to string and strip spaces
    s = str(val).strip()
    
    # Replace smart quotes and special characters to prevent SQL encoding crashes
    s = s.replace("“", '"').replace("”", '"')
    s = s.replace("‘", "'").replace("’", "'")
    s = s.replace("—", "-").replace("–", "-")
    
    # Escape single quotes for SQL safety by doubling them (e.g. ' becomes '')
    s = s.replace("'", "''")
    return s

def parse_excel():
    excel_path = "questions.csv"  # The Excel file (which is actually a renamed .xlsx)
    temp_path = "temp_questions.xlsx"  # Temporary file name with proper .xlsx extension for openpyxl
    output_sql_path = "insert_questions.sql"
    
    if not os.path.exists(excel_path):
        print(f"Error: Could not find '{excel_path}' in the folder.")
        return

    print("==================================================")
    print(f"Reading Excel file: {excel_path} (loading as temp_questions.xlsx)")
    
    try:
        # Copy file to have a proper .xlsx extension so openpyxl doesn't reject it based on name
        shutil.copyfile(excel_path, temp_path)
        
        # Load the workbook and select active sheet
        wb = openpyxl.load_workbook(temp_path, data_only=True)
        sheet = wb.active
        print(f"Active Sheet: {sheet.title}")
        print(f"Total Rows: {sheet.max_row}")
        
        sql_statements = [
            "-- ==================================================\n",
            "-- Generated SQL Inserts for Aptitude Questions\n",
            "-- ==================================================\n\n",
            "USE careerpath_db;\n\n"
        ]
        
        count = 0
        # Iterate rows starting from Row 2
        for r_idx in range(2, sheet.max_row + 1):
            row = sheet[r_idx]
            
            # Row 3 (r_idx == 3) is index 2 in 0-indexed list and has columns split by commas in the spreadsheet.
            # We handle it manually to prevent column shifting.
            if r_idx == 3:
                comp_val = "1"
                category = "LOGICAL"
                q_text = clean_text("Every day in his business, a merchant had to weigh amounts from 1 kg to 121 kg to the nearest kilogram. What is the minimum number of weights required, and what should their weights be?")
                opt_a = clean_text("4 weights (1, 2, 4, 8)")
                opt_b = clean_text("5 weights (1, 3, 9, 27, 81)")
                opt_c = clean_text("6 weights (1, 2, 4, 8, 16, 32)")
                opt_d = clean_text("7 weights (1, 3, 9, 27, 54, 81, 121)")
                corr_opt = "B"
                explanation = clean_text("Using balanced ternary, the minimum weights are 1, 3, 9, 27, and 81 kg.")
            else:
                # Normal row parsing
                comp_id = row[0].value
                category = clean_text(row[1].value).upper()
                q_text = clean_text(row[2].value)
                opt_a = clean_text(row[3].value)
                opt_b = clean_text(row[4].value)
                opt_c = clean_text(row[5].value)
                opt_d = clean_text(row[6].value)
                corr_opt = clean_text(row[7].value).upper()
                explanation = clean_text(row[8].value)
                
                # Skip empty rows
                if not q_text:
                    continue
                    
                # Handle company ID
                comp_val = "NULL"
                if comp_id is not None and str(comp_id).strip() != "":
                    try:
                        comp_val = str(int(float(comp_id)))
                    except ValueError:
                        pass
            
            # Construct insert SQL statement
            sql_line = (
                f"INSERT INTO aptitude_questions (company_id, category, question_text, "
                f"option_a, option_b, option_c, option_d, correct_option, explanation) "
                f"VALUES ({comp_val}, '{category}', '{q_text}', '{opt_a}', '{opt_b}', "
                f"'{opt_c}', '{opt_d}', '{corr_opt}', '{explanation}');\n"
            )
            sql_statements.append(sql_line)
            count += 1
            
        # Write to file
        with open(output_sql_path, "w", encoding="utf-8") as f:
            f.writelines(sql_statements)
            
        print(f"SUCCESS: Extracted {count} questions and wrote them to '{output_sql_path}'!")
        print("==================================================")
        
    except Exception as e:
        print(f"Error parsing Excel file: {e}")
    finally:
        # Clean up temporary file
        if os.path.exists(temp_path):
            os.remove(temp_path)

if __name__ == "__main__":
    parse_excel()
