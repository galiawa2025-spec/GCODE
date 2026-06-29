import SwiftUI

struct CodeConverterView: View {
    @State private var inputValue: String = ""
    @State private var resultValue: String = ""
    @State private var shakeOffset: CGFloat = 0
    
    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(colors: [Color(red: 0.06, green: 0.016, blue: 0.18), Color(red: 0.1, green: 0.02, blue: 0.25)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            VStack(spacing: 0) {
                ScrollView {
                    VStack(spacing: 32) {
                        Spacer()
                            .frame(height: 80)
                        
                        // Title with shake animation
                        Text("GALIAWA CODE")
                            .font(.system(size: 32, weight: .black, design: .default))
                            .foregroundColor(Color(red: 0.06, green: 0.016, blue: 0.18))
                            .shadow(color: Color.black.opacity(0.3), radius: 8, x: 4, y: 4)
                            .offset(x: shakeOffset)
                            .onAppear {
                                startShakeAnimation()
                            }
                            .padding(.bottom, 60)
                        
                        // Input label
                        Text("کۆد بنوسە")
                            .font(.system(size: 18, weight: .bold))
                            .foregroundColor(.gray)
                            .frame(maxWidth: .infinity, alignment: .center)
                            .padding(.bottom, 8)
                        
                        // Input TextField
                        TextField("بنوسە...", text: $inputValue)
                            .onChange(of: inputValue) { newValue in
                                updateResult(input: newValue)
                            }
                            .font(.system(size: 20, weight: .bold, design: .default))
                            .multilineTextAlignment(.center)
                            .padding(16)
                            .background(Color.white)
                            .cornerRadius(12)
                            .overlay(
                                RoundedRectangle(cornerRadius: 12)
                                    .stroke(Color(red: 0.06, green: 0.016, blue: 0.18), lineWidth: 2)
                            )
                            .padding(.horizontal)
                        
                        Spacer()
                            .frame(height: 32)
                        
                        // Output box
                        VStack {
                            Text(resultValue)
                                .font(.system(size: 80, weight: .black, design: .default))
                                .foregroundColor(Color(red: 0.06, green: 0.016, blue: 0.18))
                                .minimumScaleFactor(0.5)
                                .lineLimit(1)
                        }
                        .frame(maxWidth: .infinity)
                        .frame(height: 140)
                        .background(Color.white)
                        .cornerRadius(12)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(Color(red: 0.06, green: 0.016, blue: 0.18), lineWidth: 2)
                        )
                        .padding(.horizontal)
                        
                        Spacer()
                            .frame(height: 40)
                    }
                    .padding(.horizontal, 24)
                }
                
                Spacer()
                
                // Footer
                Text("DEV By: GAILAN ABDULLA")
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(.red)
                    .padding(.bottom, 32)
            }
        }
    }
    
    private func updateResult(input: String) {
        let upperInput = input.uppercased().trimmingCharacters(in: .whitespaces)
        
        if let number = Int64(upperInput), number > 0 {
            resultValue = convertNumberToStringCode(number)
        } else {
            if let resultNum = convertStringCodeToNumber(upperInput) {
                resultValue = String(resultNum)
            } else {
                resultValue = ""
            }
        }
    }
    
    private func convertNumberToStringCode(_ n: Int64) -> String {
        if n <= 0 { return "" }
        let group = (n - 1) / 26 + 1
        let charIndex = Int((n - 1) % 26)
        let char = Character(UnicodeScalar(UInt8(ascii: "A") + UInt8(charIndex))!)
        return "\(group)\(char)"
    }
    
    private func convertStringCodeToNumber(_ code: String) -> Int64? {
        guard !code.isEmpty else { return nil }
        
        let lastChar = code.last!
        guard lastChar >= "A" && lastChar <= "Z" else { return nil }
        
        let numberPart = String(code.dropLast())
        guard !numberPart.isEmpty, numberPart.allSatisfy({ $0.isNumber }) else { return nil }
        
        guard let group = Int64(numberPart), group > 0 else { return nil }
        
        let charValue = Int64(lastChar.asciiValue! - Character("A").asciiValue! + 1)
        return (group - 1) * 26 + charValue
    }
    
    private func startShakeAnimation() {
        var direction: CGFloat = -1.5
        Timer.scheduledTimer(withTimeInterval: 0.075, repeats: true) { _ in
            withAnimation(.linear(duration: 0.075)) {
                shakeOffset = direction
            }
            direction *= -1
        }
    }
}

#Preview {
    CodeConverterView()
}
